<template>
	<div style="height: 100%;">
		<index-aside :collapse="collapse" :class="collapse?'index-aside-collapse':'index-aside'"></index-aside>
		<el-main class="main_view main-container index_transition" style="max-width:100%" :class="collapse?'main_view-collapse':''">
			<index-header class="index_header index_transition" :collapse="collapse"
				@collapseChange="collapseChange" :style="{'width':'100%','max-width':'100%'}">
			</index-header>
			<router-view class="router-view index_transition">
			</router-view>
		</el-main>
	</div>
</template>

<script setup>
	import IndexAside from '@/components/index/indexMenu'
	import IndexHeader from '@/components/index/indexTop'
	import IndexTags from '@/components/index/indexTags'
	import menu from "@/utils/menu";
	import router from '../../router'
	import {
		ref,
		getCurrentInstance
	} from 'vue'
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const collapse = ref( false)
	const collapseChange = () => {
		collapse.value = !collapse.value
	}
	const menuList = ref(null)
	const role = ref('')
	const init = () => {
		const menus = menu.list()
		menuList.value = Array.isArray(menus) ? menus : []
		role.value = context?.$toolUtil.storageGet('role')
		if (!Array.isArray(menuList.value) || menuList.value.length === 0 || !role.value) {
			return
		}
		for (let i = 0; i < menuList.value.length; i++) {
			if (menuList.value[i].roleName == role.value) {
				menuList.value = menuList.value[i].backMenu;
				break;
			}
		}
		let children = makeMenu(Array.isArray(menuList.value) ? menuList.value : [])
		for (let i = 0; i < children.length; i++) {
			if (!router.hasRoute(children[i].name)) {
				router.addRoute('Index', children[i])
			}
		}
	}
	const makeMenu = (menu) => {
		let children = []
		if (!Array.isArray(menu)) return children
		for (let x in menu) {
			if (!menu[x] || !Array.isArray(menu[x].child)) continue
			for (let i in menu[x].child) {
				children.push({
					path: '/' + menu[x].child[i].tableName,
					name: menu[x].child[i].menu,
					component: () => import(`@/views/${menu[x].child[i].tableName}/list.vue`)
				})
			}
		}
		return children
	}
	init()
</script>
<style lang="scss" scoped>
	a:hover {
		background: #00c292;
	}

	.el-main {
	}
	.main_view-collapse {
		padding: 80px 0px 0 0px !important;
		margin: 0 0 0 0px;
		overflow: hidden;
		background: rgba(23,139,23,.1);
		transition: all 0.3s ease-in-out 0s;
	}
	.main_view {
		position: relative;
		padding:0;
	}

	.index-aside {
	}
	.index-aside-collapse {
		box-shadow: 0px 0 0px  rgba(0, 0, 0, .3);
		overflow: hidden;
		top: 80px;
		left: 0;
		background: #303639;
		width: 0px;
		position: fixed;
		height: 100%;
	}

	.index_header {
		width: 100%;
		z-index: 999;
	}

	.index_tags {
		width: 100%;
		z-index: 999;
	}

	.index_transition{
		transition:all .35s;
	}
</style>
