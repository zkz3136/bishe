<template>
	<div>
		<div class="center_view edit_form">
			<el-form class="userinfo_form" ref="userinfoFormRef" :model="form">
				<el-row>
					<el-col :span="24">
						<el-form-item label="头像" prop="avatar" class="avatar_item">
							<uploads
								action="file/upload" 
								tip="请上传头像"
								style="width: 100%;text-align: left;"
								:fileUrls="user.avatar?user.avatar:''" 
								@change="avatarUploadSuccess"
								uploadLabel="更换">
							</uploads>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="账号" prop="login_name">
							<el-input class="list_inp" v-model="user.login_name" readonly placeholder="账号" clearable />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="名称" prop="name">
							<el-input class="list_inp" v-model="user.name"  placeholder="名称" clearable />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="性别" prop="gender">
							<el-select 
								class="list_sel" 
								v-model="user.gender" 
								placeholder="请选择性别"
								>
								<el-option v-for="item in genderOptions" :label="item" :value="item"></el-option>
							</el-select>
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="手机号码" prop="mobile">
							<el-input class="list_inp" v-model="user.mobile"  placeholder="手机号码" clearable />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="密保问题" prop="security_question">
							<el-input class="list_inp" v-model="user.security_question"  placeholder="密保问题" clearable />
						</el-form-item>
					</el-col>
					<el-col :span="12">
						<el-form-item label="密保答案" prop="security_answer">
							<el-input class="list_inp" v-model="user.security_answer"  placeholder="密保答案" clearable />
						</el-form-item>
					</el-col>
					<span class="formModel_btn_box">
						<el-button class='confirm_btn' type="primary" @click="onSubmit">保存</el-button>
					</span>
				</el-row>
			</el-form>
		</div>
	</div>
</template>

<script setup>
	import { isNumber,isIntNumer,isEmail,isMobile,isPhone,isURL,checkIdCard } from "@/utils/toolUtil";
	import {
		reactive,
		ref,
		getCurrentInstance
	} from 'vue'
	import { useStore } from 'vuex'
	const store = useStore()
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const tableName = ref('user')
	const user = ref({})
	const genderOptions = ref([])
	const init = () => {
		genderOptions.value = "男,女".split(',')
	}
	const avatarUploadSuccess=(fileUrls)=> {
	    user.value.avatar = fileUrls;
	}
	const onSubmit = () => {
		if((!user.value.login_name)){
			context?.$toolUtil.message(`账号不能为空`,'error')
			return false
		}
		if(user.value.avatar!=null){
			user.value.avatar = user.value.avatar.replace(new RegExp(context?.$config.url,"g"),"");
		}
		if((!user.value.password)){
			context?.$toolUtil.message(`密码不能为空`,'error')
			return false
		}
		if((user.value.mobile)&&(!context?.$toolUtil.isMobile(user.value.mobile))){
			context?.$toolUtil.message(`手机号码应输入手机格式`,'error')
			return false
		}
		if((user.value.maxPasswordWrong)&&(!context?.$toolUtil.isIntNumer(user.value.maxPasswordWrong))){
			context?.$toolUtil.message(`最大密码输错次数应输入整数`,'error')
			return false
		}
		if((user.value.isLocked)&&(!context?.$toolUtil.isIntNumer(user.value.isLocked))){
			context?.$toolUtil.message(`用户锁定状态应输入整数`,'error')
			return false
		}
		if((user.value.balance)&&(!context?.$toolUtil.isNumber(user.value.balance))){
			context?.$toolUtil.message(`余额应输入数字`,'error')
			return false
		}
		store.dispatch('user/update',user.value).then(res=>{
			if(res?.data&&res.data.code==0)context?.$toolUtil.message('修改成功','success')
		})

	}
	const getInfo = () => {
		context?.$http({
			url: `${tableName.value}/session`,
			method: 'get'
		}).then(res => {
			user.value = res.data.data
			init()
		})
	}
	getInfo()
</script>

<style lang="scss" scoped>
	// 表单
	.userinfo_form {
		// form item
		:deep(.el-form-item) {
			// 内容盒子
			.el-form-item__content{
			}
			// 输入框
			.list_inp {
			}
            &.avatar_item{
                .el-form-item__label{
                    display: none;
                }
                .el-form-item__content{
                    margin-left: 0 !important;
                    display: flex;
                    justify-content: center;
                }
            }
			//下拉框样式
			.list_sel {
				//去掉默认样式
				.select-trigger{
					height: 100%;
					.el-input{
						height: 100%;
					}
				}
			}
			//图片上传样式
				.el-upload-list  {
                    display: flex;
                    flex-direction: column;
					//提示语
					.el-upload__tip {
					}
					//外部盒子
				.el-upload--picture-card {
                    display: flex;
                    flex-direction: column;
                    align-items: center;
                    width: 100px;
                    height: auto;
					//图标
					.el-icon{
					}
                    .upload-text-mode {
                        width: 100%;
                        display: flex;
                        align-items: center;
                        border: none;
                        margin-top: 10px;
                        .el-button{
                            width: 100%;
                        }
                    }
                    border: none;
                    background-color: transparent;
				}
				.el-upload-list__item {
                    width: 100px;
                    height: 100px;
                    margin: 0;
                    border: none;
                    border-radius: 20px;
				}
			}

		}
		// 按钮盒子
		.formModel_btn_box {
			// 确定按钮
			.confirm_btn {
			}
			// 确定按钮-悬浮
			.confirm_btn:hover {
			}
		}
	}
</style>
