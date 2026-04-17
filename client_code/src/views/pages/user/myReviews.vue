<template>
	<div class="list-page" :style='{}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="Ξ" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb">我的评价</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
            <div class="back_view">
                <el-button class="back_btn" @click="backClick" type="primary">返回</el-button>
            </div>
        </div>
		<el-tabs v-model="activeName" type="card" class="demo-tabs" @tab-change="handleClick" style="width: 100%;">
			<el-tab-pane label="美食评价" name="dish_info"></el-tab-pane>
		</el-tabs>

		<div class="page_list">
			<div class="data_box">
				<el-table :data="list" border style="width: 100%" v-loading="listLoading" :stripe='true'>
					<el-table-column label="菜品名称" prop="name" width="150"></el-table-column>
					<el-table-column label="菜品图片" width="120">
						<template #default="scope">
							<div v-if="scope.row.image">
								<el-image :src="$config.url + scope.row.image.split(',')[0]" style="width: 100px; height: 100px; object-fit: cover;"></el-image>
							</div>
							<div v-else>无图片</div>
						</template>
					</el-table-column>
					<el-table-column prop="content" label="评价内容">
                        <template #default="scope">
                           <div v-html="scope.row.content"></div>
                        </template>
                   </el-table-column>
					<el-table-column prop="reply" label="餐厅回复" width="240">
						<template #default="scope">
							<div v-if="scope.row.reply">{{ sanitizeReply(scope.row.reply) }}</div>
							<div v-else>—</div>
						</template>
					</el-table-column>
					<el-table-column prop="score" label="评分" width="100"></el-table-column>
					<el-table-column prop="addtime" label="评价时间" width="180"></el-table-column>
					<el-table-column label="操作" width="180">
						<template #default="scope">
							<div style="display: flex; align-items: center; gap: 10px;">
								<el-button class="view_btn" type="primary" size="small" @click="toDetail(scope.row)">查看详情</el-button>
								<el-button class="del_btn" type="danger" size="small" @click="delClick(scope.row)">删除</el-button>
							</div>
						</template>
					</el-table-column>
				</el-table>
				<el-pagination
					background
					layout="total, prev, pager, next, sizes, jumper"
					:total="total"
					:page-size="listQuery.limit"
                    v-model:current-page="listQuery.page"
					@size-change="sizeChange"
					@current-change="currentChange"
				/>
			</div>
		</div>
	</div>
</template>

<script setup>
	import {
		ref,
		getCurrentInstance,
		onMounted,
	} from 'vue';
	import {
		ElMessageBox
	} from 'element-plus'
	import {
		useRouter
	} from 'vue-router';
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const router = useRouter()
	
    
    const activeName = ref('dish_info')
	const list = ref([])
	const listLoading = ref(false)
	const listQuery = ref({
		page: 1,
		limit: 10,
		user_id: context?.$toolUtil.storageGet('user_id'),
        sort: 'id',
        order: 'desc'
	})
	const total = ref(0)

    const backClick = () => {
        router.push(`/index/${context?.$toolUtil.storageGet('frontSessionTable')}Center`)
    }

    const handleClick = () => {
        listQuery.value.page = 1
        getList()
    }

	const getList = () => {
		listLoading.value = true
		context?.$http({
			url: `discuss${activeName.value}/list`,
			method: 'get',
			params: listQuery.value
		}).then(async res => {
			let rows = res.data.data.list
            // Fetch details
            const promises = rows.map(async (row) => {
                 try {
                     const infoRes = await context?.$http.get(`${activeName.value}/info/${row.ref_id}`)
                     const info = infoRes.data.data
                     row.name = info.dish_name
                     row.image = info.dish_image
                 } catch (e) {
                     // console.error(e)
                     row.name = '未知'
                     row.image = ''
                 }
                 return row
            })
            list.value = await Promise.all(promises)
			total.value = Number(res.data.data.total)
            listLoading.value = false
		})
	}
	const sanitizeReply = (h) => {
		let s = String(h ?? '').trim()
		s = s.replace(/^(管理员：|员工：|餐厅回复：)\s*/, '')
		s = s.replace(/<br\s*\/?>/gi, '\n')
		s = s.replace(/<\/p>/gi, '\n')
		s = s.replace(/<[^>]+>/g, '')
		return s.trim()
	}
    
    const toDetail = (row) => {
        router.push(`/index/${activeName.value}Detail?id=${row.ref_id}`)
    }

	const delClick = (row) => {
        ElMessageBox.confirm(`是否删除该评价？`, '提示', {
            confirmButtonText: '是',
            cancelButtonText: '否',
            type: 'warning',
        }).then(() => {
            context?.$http({
                url: `discuss${activeName.value}/delete`,
                method: 'post',
                data: [row.id]
            }).then(res => {
                context?.$toolUtil.message('删除成功', 'success')
                // 更新对应商品的评价数
                context.$http.get(`${activeName.value}/info/${row.ref_id}`).then(res=>{
                    let detail = res.data.data
                    detail.discussNumber--
                    context.$http.post(`${activeName.value}/update`,detail)
                })
                getList()
            })
        }).catch(_ => {})
	}

	const sizeChange = (size) => {
		listQuery.value.limit = size
		getList()
	}
	const currentChange = (page) => {
		listQuery.value.page = page
		getList()
	}

    onMounted(()=>{
        getList()
    })
</script>

<style lang="scss" scoped>
	// 返回盒子
	.back_view {
		border-radius: 4px;
		padding: 10px 0px;
		margin: 10px auto;
		background: none;
		width: 100%;
		text-align: left;
		// 返回按钮
		.back_btn {
			border: 1px solid var(--theme-color);
			cursor: pointer;
			border-radius: 0px;
			padding: 0 24px;
			color: #fff;
			background: var(--theme-color);
			width: auto;
			font-size: 14px;
			height: 34px;
		}
		// 返回按钮-悬浮
		.back_btn:hover {
		}
	}
    .demo-tabs {
		// 头部
		:deep(.el-tabs__header) {
			border: 0px solid #e4e7ed;
			padding: 0;
			box-shadow: none;
			margin: 15px 0;
			width: 100%;
			border-width: 1px 0;
			line-height: auto;
			height: auto;
			// 滑动区
			.el-tabs__nav-scroll {
				border: 0;
				display: flex;
				width: 100%;
				justify-content: center;
				// list
				.el-tabs__nav {
					border: 0;
					margin: 30px auto;
					width: 100%;
					text-align: center;
					// item
					.el-tabs__item {
						border: 1px solid #e4e7ed;
						padding: 0 20px;
						margin: 0 10px 0 0;
						color: #333;
						background: #fff;
						font-size: 16px;
						line-height: 40px;
						transition: all 0s;
						height: 40px;
					}
					// item active
					.is-active {
						border: 1px solid var(--theme);
						padding: 0 20px;
						margin: 0 10px 0 0;
						color: #fff;
						background: var(--theme);
						font-size: 16px;
						border-color: #fff;
						line-height: 40px;
						height: 40px;
					}
					// item hover
					.el-tabs__item:hover {
						border: 1px solid var(--theme);
						padding: 0 20px;
						color: #fff;
						background: var(--theme);
						border-color: #fff;
						line-height: 40px;
						height: 40px;
					}
				}
			}
		}
	}
    
    // 表格样式
	.el-table {
		padding: 0;
		margin: 20px 0 0;
		background: #fff;
		width: 100%;
		font-size: 15px;
		border-color: #eee;
		border-width: 1px 0 0 1px;
		border-style: solid;
		:deep(.el-table__header-wrapper) {
			thead {
				color: #333;
				font-weight: 500;
				width: 100%;
				tr {
					background: #fff;
					th {
						padding: 8px 0;
						background: var(--theme);
						border-color: #eee;
						border-width: 0 1px 1px 0;
						border-style: solid;
						text-align: left;
						.cell {
							padding: 0 10px;
							word-wrap: normal;
							color: #fff;
							white-space: nowrap;
							font-weight: bold;
							display: flex;
							vertical-align: middle;
							line-height: 24px;
							text-overflow: ellipsis;
							word-break: break-all;
							width: 100%;
							align-items: center;
							position: relative;
							min-width: 110px;
						}
					}
				}
			}
		}
		:deep(.el-table__body-wrapper) {
			tbody {
				width: 100%;
				tr {
					background: #fff;
					td {
						padding: 12px 0;
						color: #666;
						background: #fff;
						border-color: #eee;
						border-width: 0 1px 1px 0;
						border-style: solid;
						text-align: left;
						.cell {
							padding: 0 10px;
							overflow: hidden;
							word-break: break-all;
							white-space: normal;
							line-height: 24px;
							text-overflow: ellipsis;
                            // 查看详情
							.view_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 10px 0 0;
								outline: none;
								color: #fff;
								background: #2abdb8;
								width: auto;
								font-size: 14px;
								height: 32px;
							}
                            // 删除
							.del_btn {
								border: 0;
								cursor: pointer;
								border-radius: 4px;
								padding: 0 5px;
								margin: 0 10px 0 0;
								outline: none;
								color: #fff;
								background: rgba(255, 73, 73, 1);
								width: auto;
								font-size: 14px;
								min-width: 60px;
								height: 32px;
							}
						}
					}
				}
				tr.el-table__row--striped {
					td {
						background: #FAFAFA !important;
					}
				}
				tr:hover {
					td {
						padding: 12px 0;
						color: #333;
						background: rgba(245, 245, 245, 1);
						border-color: #eee;
						border-width: 0 1px 1px 0;
						border-style: solid;
						text-align: left;
					}
				}
			}
		}
	}
    // 分页器
	.el-pagination {
		// 总页码
		:deep(.el-pagination__total) {
		}
		// 上一页
		:deep(.btn-prev) {
		}
		// 下一页
		:deep(.btn-next) {
		}
		// 上一页禁用
		:deep(.btn-prev:disabled) {
		}
		// 下一页禁用
		:deep(.btn-next:disabled) {
		}
		// 页码
		:deep(.el-pager) {
			// 数字
			.number {
			}
			// 数字悬浮
			.number:hover {
			}
			// 选中
			.number.is-active {
			}
		}
		// sizes
		:deep(.el-pagination__sizes) {
			.el-select {
				//去掉默认样式
				.select-trigger{
					height: 100%;
					.el-input{
						height: 100%;

					}
				}
			}
		}
		// 跳页
		:deep(.el-pagination__jump) {
			// 输入框
			.el-input {

			}
		}
	}
</style>
