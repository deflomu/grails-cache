/*
 * Copyright 2002-2013 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package grails.plugin.cache;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.Arrays;

import org.springframework.cache.interceptor.KeyGenerator;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;

/**
 * Copy of Spring 4.0's {@link org.springframework.cache.interceptor.SimpleKeyGenerator} and {@link org.springframework.cache.interceptor.SimpleKey}
 * 
 * Simple key generator. Returns the parameter itself if a single non-null value
 * is given, otherwise returns a {@link SimpleKey} of the parameters.
 *
 * <p>Unlike DefaultKeyGenerator, no collisions will occur with the keys
 * generated by this class. The returned {@link SimpleKey} object can be safely
 * used with a {@link org.springframework.cache.concurrent.ConcurrentMapCache},
 * however, might not be suitable for all {@link org.springframework.cache.Cache}
 * implementations.
 *
 * @author Phillip Webb
 * @since 4.0
 * @see SimpleKey
 * @see org.springframework.cache.annotation.CachingConfigurer
 */
public class SimpleKeyGenerator implements KeyGenerator {
	
	/**
	 * A simple key as returned from the {@link SimpleKeyGenerator}.
	 *
	 * @author Phillip Webb
	 * @since 4.0
	 * @see SimpleKeyGenerator
	 */
	@SuppressWarnings("serial")
	public static final class SimpleKey implements Serializable {

		public static final SimpleKey EMPTY = new SimpleKey();

		private final Object[] params;


		/**
		 * Create a new {@link SimpleKey} instance.
		 * @param elements the elements of the key
		 */
		public SimpleKey(Object... elements) {
			Assert.notNull(elements, "Elements must not be null");
			this.params = new Object[elements.length];
			System.arraycopy(elements, 0, this.params, 0, elements.length);
		}


		@Override
		public boolean equals(Object obj) {
			return (this == obj || (obj instanceof SimpleKey && Arrays.deepEquals(this.params, ((SimpleKey) obj).params)));
		}

		@Override
		public int hashCode() {
			return Arrays.deepHashCode(this.params);
		}

		@Override
		public String toString() {
			return "SimpleKey [" + StringUtils.arrayToCommaDelimitedString(this.params) + "]";
		}

	}

	@Override
	public Object generate(Object target, Method method, Object... params) {
		if (params.length == 0) {
			return SimpleKey.EMPTY;
		}
		if (params.length == 1) {
			Object param = params[0];
			if (param != null && !param.getClass().isArray()) {
				return param;
			}
		}
		return new SimpleKey(params);
	}

}