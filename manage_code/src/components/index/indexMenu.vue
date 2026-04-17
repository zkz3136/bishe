<template>
	<div class="menu_wrapper">
		<div class="menu_scrollbar">
			<el-menu :default-openeds="[]" :unique-opened="true" default-active="0" class="menu_view"
				:collapse="collapse">
				<el-menu-item class="first-item" :index="0" @click="menuHandler('')">
					<i class="iconfont icon-zhuye2" v-if="collapse?false:true"></i>
					<template #title>
						<span>首页</span>
					</template>
				</el-menu-item>
                <template v-for=" (item,index) in menuList.backMenu">
                    <el-sub-menu v-if="item.child.length>1" class="first-item" :index="index+2+''">
                        <template #title>
							<el-icon v-if="collapse?false:true && isSupportGroup(item)" class="menu-el-icon"><ChatDotRound /></el-icon>
							<i class="iconfont" :class="item.fontClass" v-else-if="collapse?false:true && !item.iconImg"></i>
                            <img v-else-if="collapse?false:true && item.iconImg" :src="item.iconImg" class="menu-img-icon" alt="" @error="onMenuIconError(item)">
                            <span>{{ item.menu }}</span>
                        </template>
                        <el-menu-item class="second-item" v-for=" (child,sort) in item.child" :key="sort"
                            :index="(index+2)+'-'+sort"
                            @click="menuHandler(child.classname||child.tableName,child.menuJump)">{{ child.menu }}
                        </el-menu-item>
                    </el-sub-menu>
                    <el-menu-item v-else class="first-item" :index="index+2+''" @click="menuHandler(item.child[0].classname||item.child[0].tableName,item.child[0].menuJump)">
						<el-icon v-if="collapse?false:true && isSupportGroup(item)" class="menu-el-icon"><ChatDotRound /></el-icon>
                        <i class="iconfont" :class="item.fontClass" v-else-if="collapse?false:true && !item.iconImg"></i>
                        <img v-else-if="collapse?false:true && item.iconImg" :src="item.iconImg" class="menu-img-icon" alt="" @error="onMenuIconError(item)">
                        <template #title>
                            <span>{{ item.menu }}</span>
                        </template>
                    </el-menu-item>
                </template>
			</el-menu>
		</div>
	</div>
</template>

<script setup>
	import menu from '@/utils/menu'
	import { ChatDotRound } from '@element-plus/icons-vue'
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
	const isSupportGroup = (g) => {
		if (!g) return false
		const hasSupportChild = Array.isArray(g.child) && g.child.some(c => {
			const tn = c && (c.tableName || c.classname)
			return tn === 'support_ticket' || tn === 'support_faq'
		})
		return (g.menu && String(g.menu).includes('客服')) || hasSupportChild
	}
	const init = () => {
		const menus = menu.list()
		menuList.value = Array.isArray(menus) ? menus : []
		role.value = context?.$toolUtil.storageGet('role')
		if (!Array.isArray(menuList.value) || menuList.value.length === 0 || !role.value) {
			return
		}
		const isBlankValue = (v) => {
			if (v === null || v === undefined) return true
			const s = String(v).trim()
			return s === '' || s === 'null' || s === 'undefined'
		}

		for (let i = 0; i < menuList.value.length; i++) {
			if (menuList.value[i].roleName == role.value) {
				menuList.value = menuList.value[i];
				break;
			}
		}
		if (menuList.value && Array.isArray(menuList.value.backMenu)) {
			for (let i = 0; i < menuList.value.backMenu.length; i++) {
				const g = menuList.value.backMenu[i]
				if (!g) continue
				if (isSupportGroup(g) && isBlankValue(g.fontClass)) g.fontClass = 'icon-kefu'
			}
		}

	}
	const onMenuIconError = (item) => {
		if (!item) return
		item.iconImg = ''
	}
	const menuHandler = (name,menuJump) => {
        if(name == 'center'){
            context.$router.push(`/${role.value}Center`)
        }else if(name == 'favorites'){
            context.$router.push(`/storeup?type=${menuJump}`)
        }else if(name == 'dish_info'){
            context.$router.push(`/dish_info${menuJump?'?menuJump='+menuJump:''}`)
        }else if(name == 'restaurant_info'){
            context.$router.push(`/restaurant_info${menuJump?'?menuJump='+menuJump:''}`)
        }else if(name == 'restaurant_reservation'){
            context.$router.push(`/restaurant_reservation${menuJump?'?menuJump='+menuJump:''}`)
        }else if(name == 'parking_spot'){
            context.$router.push(`/parking_spot${menuJump?'?menuJump='+menuJump:''}`)
        }else if(name == 'dish_review'){
            context.$router.push(`/dish_review${menuJump?'?menuJump='+menuJump:''}`)
        }else if(name == 'user'){
            context.$router.push(`/user${menuJump?'?menuJump='+menuJump:''}`)
        }else if(name == 'staff'){
            context.$router.push(`/staff${menuJump?'?menuJump='+menuJump:''}`)
        }else if(name == 'restaurant_event'){
            context.$router.push(`/restaurant_event${menuJump?'?menuJump='+menuJump:''}`)
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
      > .el-menu-item span,
      > .el-sub-menu > .el-sub-menu__title span {
        font-size: 14px;
      }

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
					color: #ea580c;
					background: none;
					border-color: rgba(211,98,91,.5);
				}
				// 二级选中
				.is-active.second-item {
					color: #ea580c;
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
/* 滚动容器 */
.menu_wrapper .menu_scrollbar{
    height: 100%;
    overflow-y: auto;
    overflow-x: hidden;
    -webkit-overflow-scrolling: touch;
    padding-right: 6px;
    scrollbar-width: none; /* Firefox 隐藏滚动条 */
    -ms-overflow-style: none; /* IE/Edge 隐藏滚动条 */
}
.menu_wrapper .menu_scrollbar::-webkit-scrollbar{
    width: 0;
    height: 0; /* WebKit 隐藏滚动条 */
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
.menu_view li .iconfont,.menu_view li .el-icon-menu,.menu_view li .el-icon-location,.menu_view li .menu-el-icon{
    background:#ffffff;
    width: auto;
    padding:16px;
    font-size: 32px;
    color:#fff;
    border-radius:10px;
    line-height: 1;
    margin:0 0 5px;
}
.menu_view li .menu-el-icon{
    color:#ea580c !important;
    display: inline-flex;
    align-items: center;
    justify-content: center;
    width: 32px;
    height: 32px;
    box-sizing: content-box;
}
.menu_view li .menu-el-icon svg{
    width: 32px !important;
    height: 32px !important;
}
.menu_view li .menu-img-icon{
    background:#ffffff;
    width: auto;
    height: 32px;
    padding:16px;
    border-radius:10px;
    line-height: 1;
    margin:0 0 5px;
    object-fit: contain;
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
    color:#ea580c;
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
