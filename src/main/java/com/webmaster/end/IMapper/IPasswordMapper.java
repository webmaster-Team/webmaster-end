package com.webmaster.end.IMapper;

import org.apache.ibatis.annotations.Param;

public interface IPasswordMapper {
    /**
     * 判断该用户的密码是否存在
     * @param id 用户的Id
     * @return 是否存在
     */
    public boolean isExist(int id) ;


    /**
     * 添加加密后的数据
     * @param id 用户ID
     * @param salt 加密用的盐值
     * @param password 加密后的密码
     * @return 是否添加成功
     */
    public Boolean addPassword(@Param("id") int id, @Param("salt") String salt, @Param("password") String password);

    /**
     * 删除密码信息
     * @param id 用户ID
     * @return 返回是否删除成功
     */
    public Boolean deletePassword(int id) ;

    /**
     * 修改密码
     * @param id 用户ID
     * @param salt 加密的盐值
     * @param password 密码
     * @return
     */
    public Boolean updatePassword(@Param("id") int id, @Param("salt") String salt, @Param("password") String password) ;


    /**
     * 根据用户id来获得对应的密码
     * @param id 用户的id
     * @return 返回密码
     */
    public String getPassword(int id);


    /**
     * 根据用户id来获得对应的盐值
     * @param id 用户的id
     * @return 返回盐值
     */
    public String getSalt(int id);
}
