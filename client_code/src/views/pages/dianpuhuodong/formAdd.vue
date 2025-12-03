<template>
	<div class="edit_view" :style='{}'>
        <div class="breadcrumb-wrapper" style="width: 100%;">
            <div class="bread_view">
                <el-breadcrumb separator="Ξ" class="breadcrumb">
                    <el-breadcrumb-item class="first_breadcrumb" :to="{ path: '/' }">首页</el-breadcrumb-item>
                    <el-breadcrumb-item class="second_breadcrumb" v-for="(item,index) in breadList" :key="index">{{item.name}}</el-breadcrumb-item>
                </el-breadcrumb>
            </div>
        </div>
		<el-form ref="formRef" :model="form" class="add_form" label-width="120px" :rules="rules">
			<el-row>
				<el-col :span="12">
					<el-form-item label="活动名称" prop="huodongmingcheng">
						<el-input class="list_inp" v-model="form.huodongmingcheng" placeholder="活动名称"
							 type="text" 							:readonly="!isAdd||disabledForm.huodongmingcheng?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="24">
					<el-form-item label="活动图片" prop="huodongtupian">
						<uploads
							:disabled="!isAdd||disabledForm.huodongtupian?true:false"
							action="file/upload" 
							tip="请上传活动图片"
							style="width: 100%;text-align: left;"
							:fileUrls="form.huodongtupian?form.huodongtupian:''" 
							@change="huodongtupianUploadSuccess">
						</uploads>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="开始时间" prop="kaishishijian">
						<el-date-picker
							class="list_date"
							v-model="form.kaishishijian"
							format="YYYY-MM-DD HH:mm:ss"
							value-format="YYYY-MM-DD HH:mm:ss"
							type="datetime"
							style="width:100%;"
							:readonly="!isAdd||disabledForm.kaishishijian?true:false"
							placeholder="请选择开始时间" />
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="活动地点" prop="huodongdidian">
						<el-input class="list_inp" v-model="form.huodongdidian" placeholder="活动地点"
							 type="text" 							:readonly="!isAdd||disabledForm.huodongdidian?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="发布时间" prop="fabushijian">
						<el-date-picker
							class="list_date"
							v-model="form.fabushijian"
							format="YYYY-MM-DD HH:mm:ss"
							value-format="YYYY-MM-DD HH:mm:ss"
							type="datetime"
							style="width:100%;"
							:readonly="!isAdd||disabledForm.fabushijian?true:false"
							placeholder="请选择发布时间" />
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="结束时间" prop="reversetime">
						<el-date-picker
							class="list_date"
							v-model="form.reversetime"
							format="YYYY-MM-DD HH:mm:ss"
							value-format="YYYY-MM-DD HH:mm:ss"
							type="datetime"
							style="width:100%;"
							:readonly="!isAdd||disabledForm.reversetime?true:false"
							placeholder="请选择结束时间" />
					</el-form-item>
				</el-col>
				<el-col :span="24">
					<el-form-item label="活动内容" prop="huodongneirong">
						<editor class="list_editor" :value="form.huodongneirong" placeholder="请输入活动内容" :readonly="!isAdd||disabledForm.huodongneirong?true:false"
							@change="(e)=>editorChange(e,'huodongneirong')"></editor>
					</el-form-item>
				</el-col>
			</el-row>
			<div class="formModel_btn_box">
				<el-button class="formModel_cancel" @click="backClick">取消</el-button>
				<el-button class="formModel_confirm"
                           @click="save"
                           type="success"
				>
					保存
				</el-button>
			</div>
		</el-form>
	</div>
</template>
<script setup>
	import {
		ref,
		getCurrentInstance,
		watch,
		onUnmounted,
		onMounted,
		nextTick,
		computed
	} from 'vue';
	import {
		useRoute,
		useRouter
	} from 'vue-router';
    import moment from "moment";
	const context = getCurrentInstance()?.appContext.config.globalProperties;
	const route = useRoute()
	const router = useRouter()
	//基础信息
	const tableName = 'dianpuhuodong'
	const formName = '店铺活动'
	//基础信息
	const breadList = ref([{
		name: formName
	}])
	//获取唯一标识
	const getUUID =()=> {
      return new Date().getTime();
    }
	//form表单
	const form = ref({
		huodongmingcheng: '',
		huodongtupian: '',
		kaishishijian: '',
		huodongdidian: '',
		huodongneirong: '',
		fabushijian: '',
		reversetime: '',
	})
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	const disabledForm = ref({
		huodongmingcheng : false,
		huodongtupian : false,
		kaishishijian : false,
		huodongdidian : false,
		huodongneirong : false,
		fabushijian : false,
		reversetime : false,
	})
	const isAdd = ref(false)
	//表单验证
	const rules = ref({
		huodongmingcheng: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		huodongtupian: [
		],
		kaishishijian: [
		],
		huodongdidian: [
		],
		huodongneirong: [
		],
		fabushijian: [
		],
		reversetime: [
		],
	})
	//活动图片上传回调
	const huodongtupianUploadSuccess=(e)=>{
		form.value.huodongtupian = e
	}
	//methods

	//methods
	//获取info
	const getInfo = ()=>{
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			let reg=new RegExp('../../../file','g')
			res.data.data.huodongneirong = res.data.data.huodongneirong.replace(reg,'../../../cl200442145/file');
			form.value = res.data.data
		})
	}
	const crossRow = ref('')
	const crossTable = ref('')
	const crossTips = ref('')
	const crossColumnName = ref('')
	const crossColumnValue = ref('')
	//初始化
	const init = (formId=null,formType='add',formNames='',row=null,table=null,statusColumnName=null,tips=null,statusColumnValue=null) => {
			form.value.fabushijian = context?.$toolUtil.getCurDateTime()
		if(formId){
			id.value = formId
			type.value = formType
		}
		if(formType == 'add'){
			isAdd.value = true
		}else if(formType == 'info'){
			isAdd.value = false
			getInfo()
		}else if(formType == 'edit'){
			isAdd.value = true
			getInfo()
		}
		else if(formType == 'cross'){
			isAdd.value = true
			// getInfo()
			for(let x in row){
				if(x=='huodongmingcheng'){
					form.value.huodongmingcheng = row[x];
					disabledForm.value.huodongmingcheng = true;
					continue;
				}
				if(x=='huodongtupian'){
					form.value.huodongtupian = row[x];
					disabledForm.value.huodongtupian = true;
					continue;
				}
				if(x=='kaishishijian'){
					form.value.kaishishijian = row[x];
					disabledForm.value.kaishishijian = true;
					continue;
				}
				if(x=='huodongdidian'){
					form.value.huodongdidian = row[x];
					disabledForm.value.huodongdidian = true;
					continue;
				}
				if(x=='huodongneirong'){
					form.value.huodongneirong = row[x];
					disabledForm.value.huodongneirong = true;
					continue;
				}
				if(x=='fabushijian'){
					form.value.fabushijian = row[x];
					disabledForm.value.fabushijian = true;
					continue;
				}
				if(x=='reversetime'){
					form.value.reversetime = row[x];
					disabledForm.value.reversetime = true;
					continue;
				}
			}
			if(row){
				crossRow.value = row
			}
			if(table){
				crossTable.value = table
			}
			if(tips){
				crossTips.value = tips
			}
			if(statusColumnName){
				crossColumnName.value = statusColumnName
			}
			if(statusColumnValue){
				crossColumnValue.value = statusColumnValue
			}
		}
		context?.$http({
			url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
			method: 'get'
		}).then(res => {
			var json = res.data.data
            if (localStorage.getItem('autoSave')) {
                localStorage.removeItem('autoSave')
                save()
            }
		})
	}
	//初始化
	//取消
	const backClick = () => {
		history.back()
	}
	//富文本数据回调
	const editorChange = (e,name) =>{
		form.value[name] = e
	}
	//提交
	const save=()=>{
		if(form.value.huodongtupian!=null) {
			form.value.huodongtupian = form.value.huodongtupian.replace(new RegExp(context?.$config.url,"g"),"");
		}
		var table = crossTable.value
		var objcross = JSON.parse(JSON.stringify(crossRow.value))
		let crossUserId = ''
		let crossRefId = ''
		let crossOptNum = ''
		if(type.value == 'cross'){
			if(crossColumnName.value!=''){
				if(!crossColumnName.value.startsWith('[')){
					for(let o in objcross){
						if(o == crossColumnName.value){
							objcross[o] = crossColumnValue.value
						}
					}
					//修改跨表数据
					changeCrossData(objcross)
				}else{
					crossUserId = context?.$toolUtil.storageGet('userid')
					crossRefId = objcross['id']
					crossOptNum = crossColumnName.value.replace(/\[/,"").replace(/\]/,"")
				}
			}
		}
		formRef.value.validate((valid)=>{
			if(valid){
				if(crossUserId&&crossRefId){
					form.value.crossuserid = crossUserId
					form.value.crossrefid = crossRefId
					let params = {
						page: 1,
						limit: 1000, 
						crossuserid:form.value.crossuserid,
						crossrefid:form.value.crossrefid,
					}
					context?.$http({
						url: `${tableName}/page`,
						method: 'get', 
						params: params 
					}).then(res=>{
						if(res.data.data.total>=crossOptNum){
							context?.$toolUtil.message(`${crossTips.value}`,'error')
							return false
						}else{
							context?.$http({
								url: `${tableName}/${!form.value.id ? "save" : "update"}`,
								method: 'post', 
								data: form.value 
							}).then(res=>{
                                context?.$toolUtil.message(`操作成功`,'success')
                                history.back()
							})
						}
					})
				}else{
					context?.$http({
						url: `${tableName}/${!form.value.id ? "save" : "update"}`,
						method: 'post', 
						data: form.value 
					}).then(res=>{
                        context?.$toolUtil.message(`操作成功`,'success')
                        history.back()
					})
				}
			}
		})
	}
	//修改跨表数据
	const changeCrossData=(row)=>{
        if(type.value == 'cross'){
            context?.$http({
                url: `${crossTable.value}/update`,
                method: 'post',
                data: row
            }).then(res=>{})
        }
	}
	onMounted(()=>{
		type.value = route.query.type?route.query.type:'add'
		let row = null
		let table = null
		let statusColumnName = null
		let tips = null
		let statusColumnValue = null
		if(type.value == 'cross'){
			row = context?.$toolUtil.storageGet('crossObj')?JSON.parse(context?.$toolUtil.storageGet('crossObj')):{}
			table = context?.$toolUtil.storageGet('crossTable')
			statusColumnName = context?.$toolUtil.storageGet('crossStatusColumnName')
			tips = context?.$toolUtil.storageGet('crossTips')
			statusColumnValue = context?.$toolUtil.storageGet('crossStatusColumnValue')
		}
		init(route.query.id?route.query.id:null, type.value,'', row, table, statusColumnName, tips, statusColumnValue)
	})
    onUnmounted(()=>{
        Object.keys(localStorage).map(item=>{
            if(item.startsWith('cross')){
                localStorage.removeItem(item)
            }
        })
    })
</script>
<style lang="scss" scoped>
	// 面包屑盒子
	.bread_view {
		:deep(.breadcrumb) {
			.el-breadcrumb__separator {
			}
			.first_breadcrumb {
				.el-breadcrumb__inner {
				}
			}
			.second_breadcrumb {
				.el-breadcrumb__inner {
				}
			}
		}
	}
	// 表单
	.add_form{
		// form item
		:deep(.el-form-item) {
			//label
			.el-form-item__label {
			}
			// 内容盒子
			.el-form-item__content {
				// 输入框
				.list_inp {

				}
				//日期选择器
				.list_date {
				}
				// 富文本
				.list_editor {
				}
				//图片上传样式
				.el-upload-list  {
					//提示语
					.el-upload__tip {
					}
					//外部盒子
					.el-upload--picture-card {
						//图标
						.el-icon{
						}
					}
					.el-upload-list__item {
					}
				}
			}
		}
	}
	// 按钮盒子
	.formModel_btn_box {
		.formModel_cancel {
		}
		.formModel_cancel:hover {
		}
		
		.formModel_confirm {
		}
		.formModel_confirm:hover {
		}
	}
</style>
<style>
.edit_view {
    width: 1200px;
    margin: 20px auto;
    padding: 40px 20px 20px ;
    background: #fff;
    overflow: hidden;
    border: 0px solid #eee;
    font-size: 16px;
    color:#666;
}
.edit_view .add_form{
    width: 100%;
    padding: 30px;
    border:0px solid #eee
}
.edit_view .add_form .el-form-item{
    margin: 0px 0px 20px;
    display: flex;
}
.edit_view .add_form .el-form-item .el-form-item__label{
    width: 150px;
    background: none;
    text-align: right;
    display: block;
    font-size: 16px;
    color: rgb(51, 51, 51);
    font-weight: 500;
}
.edit_view .add_form .el-form-item .el-form-item__content{
    display: flex;
    justify-content: flex-start;
    align-items: center;
    flex-wrap: wrap;
    width: calc(100% - 120px);
}
.edit_view .add_form .el-form-item .el-form-item__content .list_inp{
    height: 36px;
    line-height: 36px;
    border: 1px solid rgb(221, 221, 221);
    padding: 0px 10px;
    width: 100%;
    box-sizing: border-box;
    background: rgb(255, 255, 255);
    font-size: 16px;
}


.edit_view .add_form .el-form-item .el-form-item__content .list_date{
    line-height: 36px;
    border: 1px solid rgb(221, 221, 221);
    box-sizing: border-box;
    width: 100%;
    border-radius: 0px;
    background: rgb(255, 255, 255);
    font-size: 16px;
}






.edit_view .add_form .el-form-item .el-form-item__content .el-upload--picture-card{
    background-color: rgb(255, 255, 255);
    width: 100px;
    height: 90px;
    line-height: 100px;
    text-align: center;
    border: 1px solid rgb(221, 221, 221);
    border-radius: 0px;
    cursor: pointer;
}

.edit_view .add_form .el-form-item .el-form-item__content .el-upload--picture-card .el-icon{
    font-size: 32px;
    color: #999;
}
.edit_view .add_form .el-form-item .el-form-item__content .el-upload--picture-card .el-icon svg{
    color: #999;
}


.edit_view .add_form .el-form-item .el-form-item__content .img-uploader .el-upload__tip{
    font-size: 15px;
    color: #666;
    margin: 0;
}


.edit_view .add_form .el-form-item .el-form-item__content .list_editor{
    width: 100%;
    height: auto;
    margin: 0px;
    padding: 0px;
    border-radius: 0px;
    background: rgb(255, 255, 255);
    border: 1px solid rgb(221, 221, 221);
    min-height: 300px;
}

</style>