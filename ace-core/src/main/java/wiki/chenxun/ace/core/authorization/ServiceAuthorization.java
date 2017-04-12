package wiki.chenxun.ace.core.authorization;
/**
 * 权限二进制码由2部分组成,21位的服务ID(最大可以支持2^20的ID，只能有一个),43位的方法权限代码(最多同时支持43个方法权限)
 * @author RJH 
 * @date 2017年4月12日 下午9:06:43
 */
public class ServiceAuthorization {

	private final long methodBits=43L;
	/**
	 * 注册方法
	 * @param serviceId
	 * @param methodCode
	 * @return
	 */
	public long register(long serviceId,long methodCode){
		return serviceId<<methodBits|methodCode;
	}
	/**
	 * 服务方法授权
	 * @param userCode
	 * @param methodCode
	 * @return
	 */
	public long grant(long userCode,long methodCode){
		return userCode|methodCode;
	}
	/**
	 * 取消方法授权
	 * @param userCode
	 * @param methodCode
	 * @return
	 */
	public long cancelMethodCode(long userCode,long methodCode){
		return userCode&(~methodCode);
	}
	/**
	 * 检查是否是对应的服务ID
	 * @param userCode
	 * @param serviceId
	 * @return
	 */
	public boolean checkServiceId(long userCode,long serviceId){
		return userCode>>methodBits==serviceId;
	}
	/**
	 * 检查用户授权码(userCode)是否在服务授权码中授权
	 * @param serviceCode 服务授权码
	 * @param userCode 用户授权码
	 * @return
	 */
	public boolean checkUserCode(long serviceCode,long userCode){
		return (serviceCode&userCode)==userCode;
	}
	/**
	 * 检查用户对某个方法是否允许调用
	 * @param userCode
	 * @param methodCode
	 * @return
	 */
	public boolean checkMethodCode(long userCode,long methodCode){
		return (userCode&methodCode)==methodCode;
	}
}
