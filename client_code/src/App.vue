<template>
	<router-view />
</template>
<script>
	const debounce = (fn, delay) => {
		let timer = null;
		const wrapped = function() {
			let context = this;
			let args = arguments;
			clearTimeout(timer);
      timer = setTimeout(function() {
        fn.apply(context, args);
      }, delay);
		}
		wrapped.cancel = () => {
			clearTimeout(timer);
			timer = null;
		}
		return wrapped
	}

	const _ResizeObserver = window.ResizeObserver;
	if (_ResizeObserver) {
		window.ResizeObserver = class ResizeObserver extends _ResizeObserver {
			constructor(callback) {
				const wrapped = debounce(callback, 16);
				super(wrapped);
				this.__debouncedCallback = wrapped;
			}
			disconnect() {
				if (this.__debouncedCallback && this.__debouncedCallback.cancel) {
					this.__debouncedCallback.cancel();
				}
				return super.disconnect();
			}
			unobserve(target) {
				if (this.__debouncedCallback && this.__debouncedCallback.cancel) {
					this.__debouncedCallback.cancel();
				}
				return super.unobserve(target);
			}
		}
	}
</script>
<style lang="scss">
	body {
		margin: 0;
	}
	* {
		box-sizing: border-box;
	}
	.section_title {
	}

	#app {
		font-family: Avenir, Helvetica, Arial, sans-serif;
		-webkit-font-smoothing: antialiased;
		-moz-osx-font-smoothing: grayscale;
		// text-align: center;
		color: #2c3e50;
	}

	nav {
		padding: 30px;

		a {
			font-weight: bold;
			color: #2c3e50;

			&.router-link-exact-active {
				color: #42b983;
			}
		}
	}
</style>
