<template>
	<div class="menu_wrapper">
		<el-scrollbar wrap-class="scrollbar-wrapper" class="menu_scrollbar">
			<el-menu :default-openeds="[]" :unique-opened="true" default-active="0" class="menu_view"
				:collapse="collapse">
				<el-menu-item class="first-item" :index="0" @click="menuHandler('')">
					<i class="iconfont icon-zhuye2" v-if="collapse?false:true"></i>
					<template #title>
						<span>首页</span>
					</template>
				</el-menu-item>
                <template v-for=" (item,index) in menuList.backMenu">
                    <el-sub-menu   class="first-item" :index="index+2+''">
                        <template #title>
                            <i class="iconfont" :class="item.fontClass" v-if="collapse?false:true"></i>
                            <span>{{ item.menu }}</span>
                        </template>
                        <el-menu-item class="second-item" v-for=" (child,sort) in item.child" :key="sort"
                            :index="(index+2)+'-'+sort"
                            @click="menuHandler(child.classname||child.tableName,child.menuJump)">{{ child.menu }}
                        </el-menu-item>
                    </el-sub-menu>
                </template>
			</el-menu>
		</el-scrollbar>
	</div>
</template>

<script setup>
	import menu from '@/utils/menu'
	import {
		ref,
		toRefs,
		getCurrentInstance,
		nextTick
	} from 'vue';
	import { useStore } from 'vuex'
	const store = useStore()
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	//props
	const props = defineProps({
		collapse: Boolean
	})
	const {
		collapse
	} = toRefs(props)
	//data
	const menuList = ref([])
	const role = ref('')
	//权限验证
	const btnAuth = (e,a)=>{
		return context?.$toolUtil.isAuth(e,a)
	}
	const init = () => {
		const menus = menu.list()
		if (menus) {
			menuList.value = menus
		}
		role.value = context?.$toolUtil.storageGet('role')

		for (let i = 0; i < menuList.value.length; i++) {
			if (menuList.value[i].roleName == role.value) {
				menuList.value = menuList.value[i];
				break;
			}
		}
	}
	const menuHandler = (name,menuJump) => {
        if(name == 'center'){
            context.$router.push(`/${role.value}Center`)
        }else if(name == 'storeup'){
            context.$router.push(`/storeup?type=${menuJump}`)
        }else if(name == 'exampaper' && menuJump == '12'){
            context.$router.push('/exampaperlist')
        }else if(name == 'examrecord' && menuJump == '22'){
            context.$router.push('/examfailrecord')
        }else{
            context.$router.push(`/${name}${menuJump?'?menuJump='+menuJump:''}`)
        }
	}
	init()
</script>

<style lang="scss" scoped>
	// 总盒子
	:deep(.menu_scrollbar) {

		// 菜单盒子-展开样式
		.menu_view {

			// 无二级菜单
			.el-menu-item {
				.iconfont {
				}
			}

			// 无二级悬浮
			.el-menu-item:hover {
				background: none;
			}

			// 无二级选中
			.el-menu-item.is-active {
				background: none;
			}

			// 有二级盒子
			.el-sub-menu {

				// 有二级item
				.el-sub-menu__title {
					.iconfont {
					}
					.el-sub-menu__icon-arrow{
						display: none;
						position: absolute;
						right: 10px;
					}
				}

				// 有二级item悬浮
				.el-sub-menu__title:hover {
					background: none;
				}
			}
			//二级选中
			.is-active {
				.el-sub-menu__title {
					background: none;
				}
			}
			// 二级盒子
			.el-menu--inline {
				padding: 0 5px 0 15px;
				margin: 10px 0 0;
				background: none;
				width: 100%;
				// 二级菜单
				.second-item {
					border: 1px solid rgba(255,255,255,.2);
					border-radius: 4px;
					padding: 0 0 0 0px;
					margin: 0 auto 5px;
					color: #eee;
					background: none;
					line-height: 32px;
					text-align: center;
					min-width: auto;
					height: 32px;
				}
				// 二级悬浮
				.second-item:hover {
					color: #d3623d;
					background: none;
					border-color: rgba(211,98,91,.5);
				}
				// 二级选中
				.is-active.second-item {
					color: #d3623d;
					background: none;
					border-color: rgba(211,98,91,.5);
					line-height: 24px;
					min-width:  auto;
				}
			}
		}
		// 菜单盒子-关闭样式
		.el-menu--collapse {
			padding: 0;
			background: none;
			height: 100%;

			// 无二级菜单
			.el-menu-item {
				border: none;
				padding: 0 10px;
				color: #ccc;
				background: none;
				line-height: 50px;
				height: 50px;
			}

			// 无二级悬浮
			.el-menu-item:hover {
				padding: 0 10px;
				color: #ccc;
				background: none;
				line-height: 50px;
				height: 50px;
			}

			// 无二级选中
			.el-menu-item.is-active {
				padding: 0 10px;
				color: #ccc;
				background: none;
				line-height: 50px;
				height: 50px;
			}

			// 有二级盒子
			.el-sub-menu {
				cursor: pointer;
				padding: 0 0;
				white-space: nowrap;
				background: none;
				position: relative;

				// 有二级item
				.el-sub-menu__title {
					border: none;
					padding: 0 10px;
					color: #ccc;
					background: none;
					line-height: 50px;
					height: 50px;
					.el-sub-menu__icon-arrow{
						margin: -3px 0 0 8px;
						color: inherit;
						vertical-align: middle;
						font-size: 12px;
						position: static;
					}
				}

				// 有二级item悬浮
				.el-sub-menu__title:hover {
					padding: 0 10px;
					color: #ccc;
					background: none;
					line-height: 50px;
					height: 50px;
				}
			}
			//二级选中
			.is-active {
				.el-sub-menu__title {
					padding: 0 10px;
					color: #ccc;
					background: none;
					line-height: 50px;
					height: 50px;
				}
			}
			// 二级盒子
			.el-menu--inline {
				border: none;
				padding: 0px;
				background: none;
				// 二级菜单
				.second-item {
					padding: 0 40px;
					color: #666;
					background: none;
					line-height: 50px;
					height: 50px;
				}
				// 二级悬浮
				.second-item:hover {
					padding: 0 40px;
					color: #f00;
					background: greenyellow;
					line-height: 50px;
					height: 50px;
				}
				// 二级选中
				.is-active.second-item {
					padding: 0 40px;
					color: #000;
					background: none;
					line-height: 50px;
					height: 50px;
				}
			}
		}
	}
</style>
<style lang="scss">
	.el-menu-item .el-menu-tooltip__trigger {
		padding: 0;
	}
	.el-popper{
		.el-menu--popup-container {
			.el-menu--popup{
				border: none;
				padding: 0px;
				background: none;
				// 二级菜单
				.second-item {
					padding: 0 40px;
					color: #666;
					background: none;
					line-height: 50px;
					height: 50px;
				}
				// 二级悬浮
				.second-item:hover {
					padding: 0 40px;
					color: #f00;
					background: greenyellow;
					line-height: 50px;
					height: 50px;
				}
				// 二级选中
				.is-active.second-item {
					padding: 0 40px;
					color: #000;
					background: none;
					line-height: 50px;
					height: 50px;
				}
			}
		}
	}
</style>
<style>
/* 菜单展开样式 */
/* 中间内容总盒子 */
.main-container{
    margin: 0px 0px 0px 160px;
    padding: 80px 0px 0px !important;
    background: #f8f8f8;
    overflow: hidden;
}

/* 总盒子 */
.menu_wrapper{
    width: 160px;
    background: #343434;
    height: 100%;
    overflow: hidden;
    position: fixed;
    top: 80px;
    left: 0px;
    border-width: 0 0px 0 0;
    border-style: solid;
    border-color: rgb(82, 147, 211);
    padding: 28px 10px 60px 0;
    transition: all 0.3s ease-in-out 0s;
    box-shadow: 0px 0 0px 0 rgba(69, 90, 100, 0.2);
    z-index:9;
}
/* 一级ul */
.menu_wrapper .menu_view{
    width: 100%;
    display: flex;
    flex-wrap: wrap;
    align-items: flex-start;
    border: none;
    background: none;
    padding: 0px 0px 60px;
}

/* item */
.menu_view li{
    width: 100%;
    margin:0 0 20px;
    padding: 0px;
    background:none;
    height: auto;
    line-height: 1.5;
    display: flex;
    flex-flow: column wrap;
    align-items: center;
    justify-content: center;
    position: relative;
    color: rgba(255, 255, 255,1);
    font-size: 16px;
}
.menu_view li:hover{
    color: rgba(255, 255, 255,1);
}
.menu_view li.is-active{
    color: rgba(255, 255, 255,1);
}
/* item 盒子 */
.menu_view li .el-sub-menu__title{
    padding: 0px;
    height: auto;
    line-height: 1.5;
    display: flex;
    flex-flow: column wrap;
    align-items: center;
    justify-content: center;
    position: relative;
    color: rgba(255, 255, 255,1);
}
/* 图标 */
.menu_view li .iconfont,.menu_view li .el-icon-menu,.menu_view li .el-icon-location{
    background:#ffffff;
    width: auto;
    padding:16px;
    font-size: 32px;
    color:#fff;
    border-radius:10px;
    line-height: 1;
    margin:0 0 5px;
}
/* 图标颜色 */
.menu_view li:nth-child(1) i{
    color:#d3623d;
}
.menu_view li:nth-child(2) i{
    color:#36b874;
}
.menu_view li:nth-child(3) i{
    color:#2ea6e7;
}
.menu_view li:nth-child(4) i{
    color:#3965e1;
}
.menu_view li:nth-child(5) i{
    color:#dfb528;
}
.menu_view li:nth-child(6) i{
    color:#228B22;
}
.menu_view li:nth-child(7) i{
    color:#7B68EE;
}
.menu_view li:nth-child(8) i{
    color:#808000;
}
.menu_view li:nth-child(9) i{
    color:#008080;
}
.menu_view li:nth-child(10) i{
    color:#5F9EA0;
}
.menu_view li:nth-child(2n+11) i{
    color:#d3623d;
}
.menu_view li:nth-child(2n+12) i{
    color:#36b874;
}
.menu_view li:nth-child(2n+13) i{
    color:#2ea6e7;
}
.menu_view li:nth-child(2n+14) i{
    color:#3965e1;
}
.menu_view li:nth-child(2n+15) i{
    color:#dfb528;
}
.menu_view li:nth-child(2n+16) i{
    color:#d3623d;
}
.menu_view li:nth-child(2n+17) i{
    color:#36b874;
}
.menu_view li:nth-child(2n+18) i{
    color:#2ea6e7;
}
.menu_view li:nth-child(2n+19) i{
    color:#3965e1;
}
.menu_view li:nth-child(2n+20) i{
    color:#dfb528;
}
.first-item {
    padding: 0!important;
}

.first-item .el-sub-menu__title {
    padding: 0!important;
}
</style>