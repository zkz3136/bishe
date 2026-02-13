import toolUtil from '@/utils/toolUtil'
const menu = {
	list() {
		if(toolUtil.storageGet("menus")) {
		    let m = null
            try {
                m = JSON.parse(toolUtil.storageGet("menus"))
            } catch (e) {
                m = null
            }
            const ensureButtons = (node, requiredButtons) => {
                if (!node || !Array.isArray(requiredButtons) || requiredButtons.length === 0) return
                if (!Array.isArray(node.buttons)) node.buttons = []
                if (!Array.isArray(node.allButtons)) node.allButtons = []
                for (let i = 0; i < requiredButtons.length; i++) {
                    const btn = requiredButtons[i]
                    if (!node.allButtons.includes(btn)) node.allButtons.push(btn)
                    if (!node.buttons.includes(btn)) node.buttons.push(btn)
                }
            }
            const ensureTableButtons = (roleNode, tableName, requiredButtons) => {
                if (!roleNode || !Array.isArray(roleNode.backMenu)) return
                for (let i = 0; i < roleNode.backMenu.length; i++) {
                    const group = roleNode.backMenu[i]
                    if (!group || !Array.isArray(group.child)) continue
                    for (let j = 0; j < group.child.length; j++) {
                        const child = group.child[j]
                        if (!child) continue
                        if (child.tableName === tableName) {
                            ensureButtons(child, requiredButtons)
                            return
                        }
                    }
                }
            }
            const isDishCategoryNode = (node) => {
                if (!node) return false
                if (typeof node !== 'object') return false
                return node.menu === '菜品类型' || node.tableName === 'dish_category' || node.classname === 'dish_category'
            }
            const pruneDishCategoryMenus = (node) => {
                if (!node) return
                if (Array.isArray(node)) {
                    for (let i = node.length - 1; i >= 0; i--) {
                        const item = node[i]
                        if (isDishCategoryNode(item)) {
                            node.splice(i, 1)
                            continue
                        }
                        pruneDishCategoryMenus(item)
                        if (item && Array.isArray(item.child) && item.child.length === 0) {
                            node.splice(i, 1)
                        }
                    }
                    return
                }
                if (typeof node === 'object') {
                    if (Array.isArray(node.child)) {
                        pruneDishCategoryMenus(node.child)
                    }
                    if (Array.isArray(node.backMenu)) {
                        pruneDishCategoryMenus(node.backMenu)
                    }
                }
            }
            const isBlankValue = (v) => {
                if (v === null || v === undefined) return true
                const s = String(v).trim()
                return s === '' || s === 'null' || s === 'undefined'
            }
            if (Array.isArray(m)) {
                pruneDishCategoryMenus(m)
                for (let i = 0; i < m.length; i++) {
                    const r = m[i]
                    if (!r || !Array.isArray(r.backMenu)) continue
                    // 清理餐厅信息的“查看评论”按钮
                    for (let j = 0; j < r.backMenu.length; j++) {
                        const group = r.backMenu[j]
                        if (!group || !Array.isArray(group.child)) continue
                        for (let k = 0; k < group.child.length; k++) {
                            const child = group.child[k]
                            if (!child) continue
                            const isRestaurantInfo = child.tableName === 'cantingxinxi' || child.classname === 'cantingxinxi' || child.menu === '餐厅信息'
                            if (isRestaurantInfo) {
                                if (Array.isArray(child.buttons)) {
                                    child.buttons = child.buttons.filter(b => b !== '查看评论')
                                }
                                if (Array.isArray(child.allButtons)) {
                                    child.allButtons = child.allButtons.filter(b => b !== '查看评论')
                                }
                            }
                        }
                    }
                    if (r.roleName !== '管理员' && r.roleName !== '员工') continue
                    const hasSupport = r.backMenu.some(item => item && item.menu === '客服管理')
                    if (!hasSupport) {
                        if (r.roleName === '管理员') {
                            r.backMenu.push({
                                menu: '客服管理',
                                fontClass: 'icon-kefu',
                                child: [{
                                    menu: '客服会话',
                                    buttons: ['查看', '修改'],
                                    menuJump: '列表',
                                    classname: 'support_ticket',
                                    tableName: 'support_ticket',
                                    allButtons: ['查看', '修改']
                                }, {
                                    menu: 'FAQ知识库',
                                    buttons: ['新增', '查看', '修改', '删除'],
                                    menuJump: '列表',
                                    classname: 'support_faq',
                                    tableName: 'support_faq',
                                    allButtons: ['新增', '查看', '修改', '删除']
                                }]
                            })
                        } else if (r.roleName === '员工') {
                            r.backMenu.push({
                                menu: '客服管理',
                                fontClass: 'icon-kefu',
                                child: [{
                                    menu: '我的会话',
                                    buttons: ['查看', '修改'],
                                    menuJump: '列表',
                                    classname: 'support_ticket',
                                    tableName: 'support_ticket',
                                    allButtons: ['查看', '修改']
                                }, {
                                    menu: 'FAQ知识库',
                                    buttons: ['查看'],
                                    menuJump: '列表',
                                    classname: 'support_faq',
                                    tableName: 'support_faq',
                                    allButtons: ['查看']
                                }]
                            })
                        }
                    }
                    for (let j = 0; j < r.backMenu.length; j++) {
                        const g = r.backMenu[j]
                        if (g && g.menu === '客服管理' && isBlankValue(g.fontClass)) {
                            g.fontClass = 'icon-kefu'
                        }
                        if (g && g.menu === '客服管理' && Array.isArray(g.child)) {
                            for (let k = 0; k < g.child.length; k++) {
                                const child = g.child[k]
                                if (!child) continue
                                const tn = child.tableName || child.classname
                                if (tn === 'support_ticket' && child.menu === '人工客服会话') {
                                    child.menu = '客服会话'
                                }
                            }
                        }
                    }
                    if (r.roleName === '管理员') {
                        ensureTableButtons(r, 'config', ['新增', '删除'])
                    }
                }
            }
		    return m;
		} else {
		    return null;
		}
	}
}
export default menu;
