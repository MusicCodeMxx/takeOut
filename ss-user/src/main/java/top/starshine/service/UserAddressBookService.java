package top.starshine.service;


import com.baomidou.mybatisplus.extension.service.IService;
import top.starshine.commons.entity.user.UserAddressBook;

/**
 * 用户地址簿信息表(AddressBook)表服务接口
 *
 * @author makejava
 * @since 2022-06-28 15:01:17
 */
public interface UserAddressBookService extends IService<UserAddressBook> {

    /**
     * 获取用户地址簿中的默认地址
     * @return 地址信息
     */
    UserAddressBook getDefault();

    /**
     * 设置默认地址
     * @param id 地址簿主键
     */
    void reviseDefault(Long id);

    /**
     * 添加地址
     * @param addressBook 添加地址的数据对象
     */
    void addAddress(UserAddressBook addressBook);

    /**
     * 修改地址信息
     * @param userAddressBook 地址对象
     */
    void updateAddressById(UserAddressBook userAddressBook);
}

