<template>
	<div>
		<el-dialog class="edit_form" v-model="approvalVisible" :title="'审核'" width="70%" destroy-on-close>
			<el-form ref="ruleFormRef" :model="approvalForm" label-width="120px" :rules="rules">
				<el-row>
					<el-col :span="24">
						<el-form-item label="审核回复" prop="audit_reply">
							<el-input v-model="approvalForm.audit_reply" type="textarea" />
						</el-form-item>
					</el-col>

				</el-row>
			</el-form>
			<template #footer>
				<span class="dialog-footer" v-if="audit_statusType==1">
					<el-button type="danger" @click="approvalSave('否')">拒绝</el-button>
					<el-button type="primary" @click="approvalSave('是')">
						通过
					</el-button>
				</span>
				<span class="dialog-footer" v-if="audit_statusType==2">
					<el-button type="danger" @click="approvalVisible=false">取消</el-button>
					<el-button type="primary" @click="approvalSave()">
						回复
					</el-button>
				</span>
			</template>
		</el-dialog>
	</div>
</template>

<script setup>
	import {
		ref,
		toRefs,
		getCurrentInstance
	} from 'vue';
	const emit = defineEmits(['shChange'])
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	//props
	const props = defineProps({
		tableName: String
	})
	const {
		tableName
	} = toRefs(props)
	//props

	const rules = ref({
		audit_reply: [{
			required: true,
			message: '请输入审核回复',
			trigger: 'blur'
		}, ],
	})
	const approvalForm = ref({})
	const approvalVisible = ref(false)
	//ref
	const ruleFormRef = ref(null)
	const audit_statusType = ref(1)

	const approvalClick = (row,type=1) => {
		audit_statusType.value = type
		approvalForm.value = JSON.parse(JSON.stringify(row))
		approvalVisible.value = true
	}

	//声明父级调用
	defineExpose({
		approvalClick
	})
	const approvalSave = (type) => {
		if(audit_statusType.value == 1){
			approvalForm.value.audit_status = type
		}
		ruleFormRef.value.validate((valid) => {
			if (valid) {
				let url = `${tableName.value}/update`
				context?.$http({
					url: url,
					method: 'post',
					data: approvalForm.value
				}).then(res => {

					context?.$toolUtil.message('审核成功', 'success', obj => {
						approvalVisible.value = false
					})
					emit('shChange',type,approvalForm.value)
				})
			}
		})
	}
</script>

<style>
</style>
