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
					<el-form-item label="菜品名称" prop="caipinmingcheng">
						<el-input class="list_inp" v-model="form.caipinmingcheng" placeholder="菜品名称"
							 type="text" 							:readonly="!isAdd||disabledForm.caipinmingcheng?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="24">
					<el-form-item label="菜品图片" prop="caipintupian">
						<uploads
							:disabled="!isAdd||disabledForm.caipintupian?true:false"
							action="file/upload" 
							tip="请上传菜品图片"
							style="width: 100%;text-align: left;"
							:fileUrls="form.caipintupian?form.caipintupian:''" 
							@change="caipintupianUploadSuccess">
						</uploads>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="菜品类型" prop="caipinleixing">
						<el-select
							class="list_sel"
							:disabled="!isAdd||disabledForm.caipinleixing?true:false"
							v-model="form.caipinleixing" 
							placeholder="请选择菜品类型"
							style="width:100%;"
							>
							<el-option v-for="(item,index) in caipinleixingLists" :label="item"
								:value="item"
								>
							</el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="口味" prop="kouwei">
						<el-select
							class="list_sel"
							:disabled="!isAdd||disabledForm.kouwei?true:false"
							v-model="form.kouwei" 
							placeholder="请选择口味"
							style="width:100%;"
							>
							<el-option v-for="(item,index) in kouweiLists" :label="item"
								:value="item"
								>
							</el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="推荐指数" prop="tuijianzhishu">
						<el-select
							class="list_sel"
							:disabled="!isAdd||disabledForm.tuijianzhishu?true:false"
							v-model="form.tuijianzhishu" 
							placeholder="请选择推荐指数"
							style="width:100%;"
							>
							<el-option v-for="(item,index) in tuijianzhishuLists" :label="item"
								:value="item"
								>
							</el-option>
						</el-select>
					</el-form-item>
				</el-col>
				<el-col :span="12">
					<el-form-item label="单限" prop="onelimittimes">
						<el-input class="list_inp" v-model.number="form.onelimittimes" placeholder="单限"
							 type="text" 							:readonly="!isAdd||disabledForm.onelimittimes?true:false" />
					</el-form-item>
				</el-col>

				<el-col :span="12">
					<el-form-item label="库存" prop="alllimittimes">
						<el-input class="list_inp" v-model.number="form.alllimittimes" placeholder="库存"
							 type="text" 							:readonly="!isAdd||disabledForm.alllimittimes?true:false" />
					</el-form-item>
				</el-col>

			<el-col :span="12">
				<el-form-item label="价格" prop="price">
					<el-input class="list_inp" v-model.number="form.price" placeholder="价格"
						 type="number" 							:readonly="!isAdd||disabledForm.price?true:false" />
				</el-form-item>
			</el-col>

			<el-col :span="24">
					<el-form-item label="菜品详情" prop="caipinxiangqing">
						<editor class="list_editor" :value="form.caipinxiangqing" placeholder="请输入菜品详情" :readonly="!isAdd||disabledForm.caipinxiangqing?true:false"
							@change="(e)=>editorChange(e,'caipinxiangqing')"></editor>
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
	const tableName = 'meishixinxi'
	const formName = '美食信息'
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
		caipinmingcheng: '',
		caipintupian: '',
		caipinleixing: '',
		caipinxiangqing: '',
		kouwei: '不辣',
		tuijianzhishu: '',
		clicktime: '',
		shangjiazhanghao: '',
		shangjiamingcheng: '',
	})
	const formRef = ref(null)
	const id = ref(0)
	const type = ref('')
	const disabledForm = ref({
		caipinmingcheng : false,
		caipintupian : false,
		caipinleixing : false,
		caipinxiangqing : false,
		kouwei : false,
		tuijianzhishu : false,
		clicktime : false,
		onelimittimes : false,
		alllimittimes : false,
		price : false,
		storeupNumber : false,
		discussNumber : false,
		shangjiazhanghao : false,
		shangjiamingcheng : false,
	})
	const isAdd = ref(false)
	//表单验证
	const rules = ref({
		caipinmingcheng: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		caipintupian: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		caipinleixing: [
			{required: true,message: '请输入',trigger: 'blur'}, 
		],
		caipinxiangqing: [
		],
		kouwei: [
		],
		tuijianzhishu: [
		],
		clicktime: [
		],
		onelimittimes: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		alllimittimes: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		price: [
			{ validator: context.$toolUtil.validator.number, trigger: 'blur' },
		],
		storeupNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		discussNumber: [
			{ validator: context.$toolUtil.validator.intNumber, trigger: 'blur' },
		],
		shangjiazhanghao: [
		],
		shangjiamingcheng: [
		],
	})
	//菜品图片上传回调
	const caipintupianUploadSuccess=(e)=>{
		form.value.caipintupian = e
	}
	//菜品类型列表
	const caipinleixingLists = ref([])
	//口味列表
	const kouweiLists = ref([])
	//推荐指数列表
	const tuijianzhishuLists = ref([])
	//methods

	//methods
	//获取info
	const getInfo = ()=>{
		context?.$http({
			url: `${tableName}/info/${id.value}`,
			method: 'get'
		}).then(res => {
			let reg=new RegExp('../../../file','g')
			res.data.data.caipinxiangqing = res.data.data.caipinxiangqing.replace(reg,'../../../cl200442145/file');
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
				if(x=='caipinmingcheng'){
					form.value.caipinmingcheng = row[x];
					disabledForm.value.caipinmingcheng = true;
					continue;
				}
				if(x=='caipintupian'){
					form.value.caipintupian = row[x];
					disabledForm.value.caipintupian = true;
					continue;
				}
				if(x=='caipinleixing'){
					form.value.caipinleixing = row[x];
					disabledForm.value.caipinleixing = true;
					continue;
				}
				if(x=='caipinxiangqing'){
					form.value.caipinxiangqing = row[x];
					disabledForm.value.caipinxiangqing = true;
					continue;
				}
				if(x=='kouwei'){
					form.value.kouwei = row[x];
					disabledForm.value.kouwei = true;
					continue;
				}
				if(x=='tuijianzhishu'){
					form.value.tuijianzhishu = row[x];
					disabledForm.value.tuijianzhishu = true;
					continue;
				}
				if(x=='clicktime'){
					form.value.clicktime = row[x];
					disabledForm.value.clicktime = true;
					continue;
				}
				if(x=='onelimittimes'){
					form.value.onelimittimes = row[x];
					disabledForm.value.onelimittimes = true;
					continue;
				}
				if(x=='alllimittimes'){
					form.value.alllimittimes = row[x];
					disabledForm.value.alllimittimes = true;
					continue;
				}
				if(x=='price'){
					form.value.price = row[x];
					disabledForm.value.price = true;
					continue;
				}
				if(x=='storeupNumber'){
					form.value.storeupNumber = row[x];
					disabledForm.value.storeupNumber = true;
					continue;
				}
				if(x=='discussNumber'){
					form.value.discussNumber = row[x];
					disabledForm.value.discussNumber = true;
					continue;
				}
				if(x=='shangjiazhanghao'){
					form.value.shangjiazhanghao = row[x];
					disabledForm.value.shangjiazhanghao = true;
					continue;
				}
				if(x=='shangjiamingcheng'){
					form.value.shangjiamingcheng = row[x];
					disabledForm.value.shangjiamingcheng = true;
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
			form.value.kouwei='不辣'
		}
		context?.$http({
			url: `${context?.$toolUtil.storageGet('frontSessionTable')}/session`,
			method: 'get'
		}).then(res => {
			var json = res.data.data
			if(json.hasOwnProperty('shangjiazhanghao') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
				form.value.shangjiazhanghao = json.shangjiazhanghao
				disabledForm.value.shangjiazhanghao = true;
			}
			if(json.hasOwnProperty('shangjiamingcheng') && context?.$toolUtil.storageGet("frontRole")!="管理员"){
				form.value.shangjiamingcheng = json.shangjiamingcheng
				disabledForm.value.shangjiamingcheng = true;
			}
            if (localStorage.getItem('autoSave')) {
                localStorage.removeItem('autoSave')
                save()
            }
		})
		context?.$http({
			url: `option/caipinleixing/caipinleixing`,
			method: 'get'
		}).then(res=>{
			caipinleixingLists.value = res.data.data
		})
		kouweiLists.value = "微辣,超辣,不辣".split(',')
		tuijianzhishuLists.value = "★,★★,★★★,★★★★,★★★★★".split(',')
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
		if(form.value.caipintupian!=null) {
			form.value.caipintupian = form.value.caipintupian.replace(new RegExp(context?.$config.url,"g"),"");
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
				// 下拉框
				.list_sel {
					//去掉默认样式
					.select-trigger{
						height: 100%;
						.el-input{
							height: 100%;
						}
					}
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

.edit_view .add_form .el-form-item .el-form-item__content .list_sel{
    line-height: 36px;
    border: 1px solid rgb(221, 221, 221);
    box-sizing: border-box;
    width: calc(100% - 0px);
    padding: 0px 10px;
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