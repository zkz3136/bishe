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
                        if (child.tableName === tableName || child.classname === tableName) {
                            ensureButtons(child, requiredButtons)
                            return
                        }
                    }
                }
            }
            const ensureTableMenu = (roleNode, tableName, menuLabel, anchorTableName) => {
                if (!roleNode || !Array.isArray(roleNode.backMenu)) return null
                let targetGroup = null
                for (let i = 0; i < roleNode.backMenu.length; i++) {
                    const group = roleNode.backMenu[i]
                    if (!group || !Array.isArray(group.child)) continue
                    for (let j = 0; j < group.child.length; j++) {
                        const child = group.child[j]
                        if (!child) continue
                        const tn = child.tableName || child.classname
                        if (tn === tableName) {
                            return child
                        }
                        if (anchorTableName && tn === anchorTableName) {
                            targetGroup = group
                        }
                    }
                }
                if (!targetGroup && roleNode.backMenu.length > 0) {
                    targetGroup = roleNode.backMenu[0]
                }
                if (!targetGroup) return null
                if (!Array.isArray(targetGroup.child)) targetGroup.child = []
                const newChild = {
                    menu: menuLabel,
                    buttons: ['查看'],
                    menuJump: '列表',
                    classname: tableName,
                    tableName: tableName,
                    allButtons: ['查看']
                }
                targetGroup.child.push(newChild)
                return newChild
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
            const canonicalizeGroup = (group) => {
                if (!group || !Array.isArray(group.child)) return
                const names = new Set()
                for (let i = 0; i < group.child.length; i++) {
                    const c = group.child[i]
                    const tn = c && (c.tableName || c.classname)
                    if (tn) names.add(tn)
                }
                const hasReservation = names.has('restaurant_reservation')
                const hasRestaurantInfo = names.has('restaurant_info')
                if (names.has('parking_spot')) {
                    group.menu = '车位管理'
                } else if (hasReservation && !hasRestaurantInfo) {
                    group.menu = '预约管理'
                } else if (hasRestaurantInfo) {
                    group.menu = '餐厅管理'
                } else if (names.has('dish_info') || names.has('dish_review')) {
                    group.menu = '菜品管理'
                } else if (names.has('admin') || names.has('user') || names.has('staff')) {
                    group.menu = '人员管理'
                }
            }
            const renameMenuLabels = (node) => {
                if (!node) return
                if (Array.isArray(node)) {
                    for (let i = 0; i < node.length; i++) {
                        renameMenuLabels(node[i])
                    }
                    return
                }
                if (typeof node === 'object') {
                    if (Array.isArray(node.child)) {
                        canonicalizeGroup(node)
                        for (let i = 0; i < node.child.length; i++) {
                            const c = node.child[i]
                            if (!c) continue
                            const tn = c.tableName || c.classname
                            if (tn === 'dish_review') {
                                c.menu = '菜品评价'
                            }
                            if (typeof c.menu === 'string') {
                                // 保持标签统一由表名驱动
                            }
                            if (Array.isArray(c.buttons)) {
                                c.buttons = c.buttons.map(b => b)
                            }
                            if (Array.isArray(c.allButtons)) {
                                c.allButtons = c.allButtons.map(b => b)
                            }
                        }
                    }
                    if (Array.isArray(node.child)) {
                        renameMenuLabels(node.child)
                    }
                    if (Array.isArray(node.backMenu)) {
                        renameMenuLabels(node.backMenu)
                    }
                }
            }
            if (Array.isArray(m)) {
                renameMenuLabels(m)
                pruneDishCategoryMenus(m)
                for (let i = 0; i < m.length; i++) {
                    const r = m[i]
                    if (!r || !Array.isArray(r.backMenu)) continue
                    // 将“内容运营”分组置于最后
                    const idxOps = r.backMenu.findIndex(g => g && g.menu === '内容运营')
                    if (idxOps >= 0 && idxOps !== r.backMenu.length - 1) {
                        const movedOps = r.backMenu.splice(idxOps, 1)[0]
                        r.backMenu.push(movedOps)
                    }
                    ensureTableMenu(r, 'dish_review', '菜品评价', 'dish_info')
                    ensureTableButtons(r, 'dish_info', ['下架', '上架', '查看评价'])
                    ensureTableButtons(r, 'dish_review', ['查看'])
                    // 清理餐厅信息的“查看评价”按钮
                    for (let j = 0; j < r.backMenu.length; j++) {
                        const group = r.backMenu[j]
                        if (!group || !Array.isArray(group.child)) continue
                        for (let k = 0; k < group.child.length; k++) {
                            const child = group.child[k]
                            if (!child) continue
                            const isRestaurantInfo = child.tableName === 'cantingxinxi' || child.classname === 'cantingxinxi' || child.menu === '餐厅信息'
                            if (isRestaurantInfo) {
                                const isEvalViewBtn = (x) => {
                                    const s = String(x || '').trim()
                                    return s === '查看评价' || /^查看.*评/.test(s)
                                }
                                if (Array.isArray(child.buttons)) {
                                    child.buttons = child.buttons.filter(b => !isEvalViewBtn(b))
                                }
                                if (Array.isArray(child.allButtons)) {
                                    child.allButtons = child.allButtons.filter(b => !isEvalViewBtn(b))
                                }
                            }
                        }
                    }
                    if (r.roleName !== '管理员' && r.roleName !== '员工') continue
                    if (r.roleName === '员工') {
                        ensureTableButtons(r, 'dish_info', ['新增', '查看', '修改', '删除', '下架', '上架', '查看评价'])
                        ensureTableButtons(r, 'menu', ['新增', '查看', '修改', '删除'])
                        const adminNode = Array.isArray(m) ? m.find(x => x && x.roleName === '管理员') : null
                        let adminRestaurantGroup = null
                        let adminDishGroup = null
                        if (adminNode && Array.isArray(adminNode.backMenu)) {
                            for (let jj = 0; jj < adminNode.backMenu.length; jj++) {
                                const g2 = adminNode.backMenu[jj]
                                if (!g2 || !Array.isArray(g2.child)) continue
                                const hasAdminResvOrInfo = g2.child.some(c2 => {
                                    const t2 = c2 && (c2.tableName || c2.classname)
                                    return t2 === 'restaurant_reservation' || t2 === 'restaurant_info'
                                })
                                const hasAdminDish = g2.child.some(c2 => {
                                    const t2 = c2 && (c2.tableName || c2.classname)
                                    return t2 === 'dish_info' || t2 === 'dish_review'
                                })
                                if ((g2.menu === '餐厅管理') || hasAdminResvOrInfo) {
                                    adminRestaurantGroup = g2
                                }
                                if ((g2.menu === '菜品管理') || hasAdminDish) {
                                    adminDishGroup = g2
                                }
                            }
                        }
                    // 员工端：将“餐厅预约”与“餐厅信息”合并为同一一级菜单“餐厅管理”
                        let reservationGroup = null
                        let infoGroup = null
                        for (let j = 0; j < r.backMenu.length; j++) {
                            const g = r.backMenu[j]
                            if (!g || !Array.isArray(g.child)) continue
                            const hasReservation = g.child.some(c => {
                                const tn = c && (c.tableName || c.classname)
                                return tn === 'restaurant_reservation'
                            })
                            const hasRestaurantInfo = g.child.some(c => {
                                const tn = c && (c.tableName || c.classname)
                                return tn === 'restaurant_info'
                            })
                            if (hasReservation) reservationGroup = g
                            if (hasRestaurantInfo) infoGroup = g
                        }
                        if (reservationGroup) {
                            // 将餐厅信息子菜单移入餐厅预约所在分组
                            if (infoGroup && infoGroup !== reservationGroup && Array.isArray(infoGroup.child)) {
                                for (let k = infoGroup.child.length - 1; k >= 0; k--) {
                                    const c = infoGroup.child[k]
                                    if (!c) continue
                                    const tn = c && (c.tableName || c.classname)
                                    if (tn === 'restaurant_info') {
                                        reservationGroup.child.push(c)
                                        infoGroup.child.splice(k, 1)
                                    }
                                }
                                // 如果“信息查看”分组已空则移除
                                if (!infoGroup.child || infoGroup.child.length === 0) {
                                    const idx = r.backMenu.indexOf(infoGroup)
                                    if (idx >= 0) r.backMenu.splice(idx, 1)
                                } else {
                                    // 剩余为菜品相关，重命名为“菜品管理”
                                    const hasDish = infoGroup.child.some(cc => {
                                        const t2 = cc && (cc.tableName || cc.classname)
                                        return t2 === 'dish_info' || t2 === 'dish_review'
                                    })
                                    if (hasDish) infoGroup.menu = '菜品管理'
                                    if (hasDish && adminDishGroup) {
                                        infoGroup.fontClass = adminDishGroup.fontClass || infoGroup.fontClass
                                        infoGroup.iconImg = adminDishGroup.iconImg || infoGroup.iconImg || ''
                                    }
                                }
                            }
                            // 统一一级菜单名称为“餐厅管理”
                            reservationGroup.menu = '餐厅管理'
                            if (adminRestaurantGroup) {
                                reservationGroup.fontClass = adminRestaurantGroup.fontClass || reservationGroup.fontClass
                                reservationGroup.iconImg = adminRestaurantGroup.iconImg || reservationGroup.iconImg || ''
                            }
                            const idxInfo = reservationGroup.child.findIndex(c => {
                                const tn = c && (c.tableName || c.classname)
                                return tn === 'restaurant_info'
                            })
                            const idxResv = reservationGroup.child.findIndex(c => {
                                const tn = c && (c.tableName || c.classname)
                                return tn === 'restaurant_reservation'
                            })
                            if (idxInfo >= 0 && idxResv >= 0 && idxInfo > idxResv) {
                                const t = reservationGroup.child[idxInfo]
                                reservationGroup.child[idxInfo] = reservationGroup.child[idxResv]
                                reservationGroup.child[idxResv] = t
                            }
                        }
                        const idxOrder = r.backMenu.findIndex(g => g && g.menu === '订单管理')
                        if (idxOrder >= 0) {
                            const orderGroup = r.backMenu.splice(idxOrder, 1)[0]
                            const idxRestaurant = r.backMenu.findIndex(g => g && g.menu === '餐厅管理')
                            const idxDish = r.backMenu.findIndex(g => g && g.menu === '菜品管理')
                            const insertIdx = Math.max(idxRestaurant, idxDish)
                            if (insertIdx >= 0) {
                                r.backMenu.splice(insertIdx + 1, 0, orderGroup)
                            } else {
                                r.backMenu.push(orderGroup)
                            }
                        }
                    }
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
                                    buttons: ['新增', '查看', '修改', '删除'],
                                    menuJump: '列表',
                                    classname: 'support_faq',
                                    tableName: 'support_faq',
                                    allButtons: ['新增', '查看', '修改', '删除']
                                }]
                            })
                        }
                    }
                    if (r.roleName === '管理员') {
                        const idxUser = r.backMenu.findIndex(g => g && g.menu === '人员管理')
                        if (idxUser >= 0) {
                            const userGroup = r.backMenu.splice(idxUser, 1)[0]
                            r.backMenu.push(userGroup)
                        }
                    }
                    // 员工角色：确保“车位信息管理”存在，且不包含“删除”权限
                    if (r.roleName === '员工') {
                        // 尝试从管理员菜单中复制“车位信息”所在分组的图标配置
                        let adminParkingGroup = null
                        let adminParkingChildName = '车位信息'
                        const adminNode = Array.isArray(m) ? m.find(x => x && x.roleName === '管理员') : null
                        if (adminNode && Array.isArray(adminNode.backMenu)) {
                            for (let jj = 0; jj < adminNode.backMenu.length; jj++) {
                                const g2 = adminNode.backMenu[jj]
                                if (!g2 || !Array.isArray(g2.child)) continue
                                const hasParkingChild = g2.child.some(c2 => {
                                    const tn2 = c2 && (c2.tableName || c2.classname)
                                    return tn2 === 'parking_spot'
                                })
                                if (hasParkingChild) {
                                    adminParkingGroup = g2
                                    const pc = g2.child.find(c2 => (c2 && (c2.tableName || c2.classname)) === 'parking_spot')
                                    if (pc && pc.menu) adminParkingChildName = pc.menu
                                    break
                                }
                            }
                        }
                        const hasParking = r.backMenu.some(group => {
                            if (!group || !Array.isArray(group.child)) return false
                            return group.child.some(child => {
                                if (!child) return false
                                const tn = child.tableName || child.classname
                                return tn === 'parking_spot'
                            })
                        })
                        if (!hasParking) {
                            let adminParkingIndex = -1
                            if (adminNode && Array.isArray(adminNode.backMenu) && adminParkingGroup) {
                                adminParkingIndex = adminNode.backMenu.indexOf(adminParkingGroup)
                            }
                            const newGroup = {
                                menu: adminParkingGroup && adminParkingGroup.menu ? adminParkingGroup.menu : '车位管理',
                                fontClass: adminParkingGroup && adminParkingGroup.fontClass ? adminParkingGroup.fontClass : '',
                                iconImg: adminParkingGroup && adminParkingGroup.iconImg ? adminParkingGroup.iconImg : '',
                                child: [{
                                    menu: adminParkingChildName,
                                    buttons: ['查看'],
                                    menuJump: '列表',
                                    classname: 'parking_spot',
                                    tableName: 'parking_spot',
                                    allButtons: ['查看']
                                }]
                            }
                            if (adminParkingIndex >= 0) {
                                const insertIndex = Math.min(adminParkingIndex, r.backMenu.length)
                                r.backMenu.splice(insertIndex, 0, newGroup)
                            } else {
                                r.backMenu.push(newGroup)
                            }
                        } else {
                            // 已存在时，移除“删除”并确保基本操作按钮
                            for (let j = 0; j < r.backMenu.length; j++) {
                                const group = r.backMenu[j]
                                if (!group || !Array.isArray(group.child)) continue
                                for (let k = 0; k < group.child.length; k++) {
                                    const child = group.child[k]
                                    if (!child) continue
                                    const tn = child.tableName || child.classname
                                    if (tn === 'parking_spot') {
                                        // 同步管理员图标
                                        if (adminParkingGroup) {
                                            if (!group.fontClass && adminParkingGroup.fontClass) {
                                                group.fontClass = adminParkingGroup.fontClass
                                            }
                                            if (!group.iconImg && adminParkingGroup.iconImg) {
                                                group.iconImg = adminParkingGroup.iconImg
                                            }
                                            if (!child.menu && adminParkingChildName) {
                                                child.menu = adminParkingChildName
                                            }
                                        }
                                        if (!Array.isArray(child.buttons)) child.buttons = []
                                        if (!Array.isArray(child.allButtons)) child.allButtons = []
                                        child.buttons = child.buttons.filter(b => b === '查看')
                                        child.allButtons = child.allButtons.filter(b => b === '查看')
                                        if (!child.buttons.includes('查看')) child.buttons.push('查看')
                                        if (!child.allButtons.includes('查看')) child.allButtons.push('查看')
                                    }
                                }
                            }
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
