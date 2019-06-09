package com.dream.core.pojo;

import java.io.BufferedReader;
import java.io.FileReader;

/**
 * 后台权限
 *
 */
public enum BackendAuth {	
	
		
   /************************backend*******************************/
		/**首页管理m*/
		MANAGE_INDEX(1),
		/**首页oper*/
		HOMEPAGE(1100),
		/**数据概括oper*/
		DATAREPORT(1110),
		
		/**产品管理m*/
		MANAGE_PRODUCT(2),
		/**金融产品管理oper*/
		FINANCIALPRODUCTS(1210),
		/**产品周期管理oper*/
		PRODUCTCYCLE(1220),
		/**APP配置oper*/
		APPCONFIG(1230),
		
		/**用户管理m*/
		MANAGE_USER(3),
		/**黑名单管理oper*/
		BLACKLIST(1310),
		/**用户签约卡管理oper*/
		USERBANDCARD(1320),
		/**用户列表oper*/
		USERLIST(1330),
		/**用户消息推送oper*/
		USERMESSAGELIST(1340),
		
		
		/**CMS管理m*/
		MANAGE_CMSM(4),
		/**Banner管理oper*/
		BANNER(1410),
		/**消息管理oper*/
		MESSAGE(1420),
		
		/**审核管理m*/
		MANAGE_AUDIT(5),
		/**风控订单审核oper*/
		WINDCONTROLORDER(1510),
		/**审核功能oper*/
		AUDI_FUNCTION(1511),
		/**放款功能oper*/
		LOAN_FUNCTION(1512),
		
		
		/**订单管理m*/
		MANAGE_ORDER(6),
		/**订单列表oper*/
		ORDERLIST(1610),
		/**订单操作列表oper*/
		ORDEROPER(1620),
		/**付款订单列表oper*/
		PAYORDEROPER(1630),
		/**还款列表oper*/
		REPAYMENTORDEROPER(1640),
		
		
		/**规则管理m*/
		MANAGE_RULE(7),
		/**反欺诈规则管理oper*/
		ANTIFRAUDRULE(1710),
		/**反欺诈审核记录oper*/
		ANTIFRAUDAUDIT(1720),
		
		/**系统管理m*/
		MANAGE_SYS(8),
		/**用户管理oper*/
		USERMANAGEMENT(1810),
		/**角色管理oper*/
		ROLEMANAGEMENT(1820),
		
	/************************************************文档结束*******************************************************/	
		
	;
	/**
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300001', '理财师新增', '理财师新增', '0', NULL, NULL, NULL, '1', '1', '6');
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300002', '理财师列表', '理财师列表', '0', NULL, NULL, NULL, '1', '1', '6');
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300003', '启用与停用', '启用与停用', '0', NULL, NULL, NULL, '1', '1', '6');
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300004', '理财师编辑', '理财师编辑', '0', NULL, NULL, NULL, '1', '1', '6');
	INSERT INTO `sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`, `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('1300005', '解绑用户', '解绑用户', '0', NULL, NULL, NULL, '1', '1', '6');
	*/

	long value;

	BackendAuth(long value) {
		this.value = value;
	}

	public long getValue() {
		return value;
	}

	public static void main(String[] args) throws Exception {
		BufferedReader br = new BufferedReader(
				new FileReader(
						"D:\\java\\eclipse\\STS2\\ws\\loans-today-2.0.0\\loans-today-util\\src\\main\\java\\com\\loans\\util\\core\\web\\BackendAuth.java"));
		String line;
		int i = 1;
		int i2 = 1;
		String m = "1";
//		System.out.println("-- ---------------------------------------");
//		System.out.println("-- ----------删除模块功能表--------------------");
//		System.out.println("-- ---------------------------------------");
//		System.out.println("DELETE FROM `sys_rule_moudle`;");
//		System.out.println("DELETE FROM `sys_rule_operation`;");
//		System.out.println("-- ---------------------------------------");
//		System.out.println("-- ----------开始添加模块功能表数据--------------------");
//		System.out.println("-- ---------------------------------------");
		while ((line = br.readLine()) != null) {
			if(line.contains("文档结束")){
				break;
			}
			if (line.contains("m*/")) {
				String name = line.replace("/**","").replace("m*/","").trim();
				String nextLine = br.readLine().trim();
				int begin = nextLine.indexOf("(")+1;
				int end = nextLine.indexOf(")");
				m = nextLine.substring(begin,end );
				System.out.println(m+" "+name);
				System.out.println("INSERT INTO `today_loan_data`.`sys_rule_moudle` (`id`, `top_num`, `moudle_father`, `moudle_name`, `moudle_desc`, `create_user`,"
						+ " `create_time`, `update_user`, `update_time`, `is_enable`) VALUES ("+ m+ ", "+ m+ ", NULL, '"+name+"', '"+name+"', '1', now(), NULL, NULL, '1');");
			} else if (line.contains("oper*/")) {
				String nextLine = br.readLine().trim();
				String name = line.replace("/**", "").replace("oper*/","").trim();
				String id = nextLine.substring(nextLine.indexOf("(") + 1, nextLine.indexOf(")"));
//				System.out.println(id+" "+name);
				System.out.println("INSERT INTO `today_loan_data`.`sys_rule_operation` (`id`, `oper_name`, `oper_desc`, `create_user`, `create_time`, `update_user`,"
						+ " `update_time`, `is_enable`, `moudle_id`, `top_num`) VALUES ('"+id+"', '"+name+"', '"+name+"', '1', now(), NULL, NULL, '1', '"+m+"', '"+i+++"');");
			}
		}
//		System.out.println("-- ---------------------------------------");
//		System.out.println("-- ----------开始添加超级管理员数据--------------------");
//		System.out.println("-- ---------------------------------------");
//		System.out.println("INSERT  INTO `sys_user`(`id`,`user_name`,`user_pwd`,`real_name`,`position`,`is_true`,`create_time`,`update_time`,`version`) VALUES (1,'admin','96e79218965eb72c92a549dd5a330112','超级管理员','超级管理员',1,'2017-11-06 11:07:09','2017-11-15 15:08:22',0),(2,'fk','96e79218965eb72c92a549dd5a330112','风控人员','风控人员1',1,'2017-11-07 12:19:35','2017-11-15 15:24:23',0);");
//		System.out.println("-- ----------开始添加角色数据--------------------");
//		System.out.println("INSERT  INTO `sys_role`(`id`,`role_name`,`role_desc`,`create_user`,`create_time`,`update_user`,`update_time`,`is_enable`) VALUES (1,'超级管理员','超级管理员',1,'2017-11-06 14:32:13',NULL,'2017-11-14 17:33:11',1),(2,'风控','风控管理',1,'2017-11-07 12:18:39',NULL,'2017-11-14 17:33:00',1),(3,'普通用户','普通用户',1,'2017-11-07 12:21:13',NULL,'2017-11-14 17:32:49',1);");
//		System.out.println("-- ----------开始添加用户和角色关联数据--------------------");
//		System.out.println("insert  into `sys_user_role`(`id`,`user_id`,`role_id`,`create_time`,`create_user`,`update_time`,`update_user`) values (1,1,1,'2017-11-17 16:32:05',NULL,'2017-11-17 16:32:09',NULL),(381119430131712000,2,2,'2017-11-17 16:32:33',NULL,NULL,NULL);");
	}
}
