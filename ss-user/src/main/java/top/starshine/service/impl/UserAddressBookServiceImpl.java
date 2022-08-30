package top.starshine.service.impl;


import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import com.baomidou.mybatisplus.extension.conditions.update.LambdaUpdateChainWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import top.starshine.commons.entity.user.User;
import top.starshine.commons.entity.user.UserAddressBook;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.exception.InternalServerErrorException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.status.UserStatus;
import top.starshine.commons.util.CollectionUtils;
import top.starshine.mapper.UserAddressBookMapper;
import top.starshine.service.UserAddressBookService;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 用户地址簿信息表(AddressBook)表服务实现类
 *
 * @author makejava
 * @since 2022-06-28 15:01:17
 */
@Service
public class UserAddressBookServiceImpl extends ServiceImpl<UserAddressBookMapper, UserAddressBook> implements UserAddressBookService {

    @Override
    public UserAddressBook getDefault() {
        User user = ThreadLocalCache.getNotNull();
        // 查询用户默认地址信息,查询是默认地址标识的地址信息, 并以更新时间排序, 如果结果是多个的就获取第一个返回
        List<UserAddressBook> list = new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(UserAddressBook::getUserId, user.getId())
                .eq(UserAddressBook::getIsDefault, 1)
                .eq(UserAddressBook::getIsDelete, 0)
                .orderByDesc(UserAddressBook::getUpdateTime)
                .select(UserAddressBook::getId,
                        UserAddressBook::getIsDefault,
                        UserAddressBook::getConsigneeName,
                        UserAddressBook::getPhoneNumber,
                        UserAddressBook::getSex,
                        UserAddressBook::getDetail,
                        UserAddressBook::getLabel)
                .list();
        if (CollectionUtils.isEmpty(list)) return null;
        return list.get(0);
    }

    @Override
    @Transactional
    public void reviseDefault(Long id) {
        if (null == id) throw new RuntimeException("地址主键不存在");
         Long userId = ((User)ThreadLocalCache.getNotNull()).getId();

        // 查询用户默认地址信息,查询是默认地址标识的地址信息, 并以更新时间排序, 如果结果是多个的就获取第一个返回
        List<UserAddressBook> list = new LambdaQueryChainWrapper<>(getBaseMapper())
                .eq(UserAddressBook::getUserId, userId)
                .eq(UserAddressBook::getIsDefault, 1)
                .eq(UserAddressBook::getIsDelete, 0)
                .select(UserAddressBook::getId)
                .list();

        boolean isMeDef = true;

        // 将非目标主键都移除默认地址值
        if (CollectionUtils.isNotEmpty(list)){
            // 如果不为空就添加入进去
            for (UserAddressBook uab : list) {
                if (id.equals(uab.getId())){
                    uab.setIsDefault(1); // 设置为默认
                    isMeDef = false;// 如果当前列表中有目标对象，就不执行设置默认地址，走批量设置
                }else{
                    uab.setIsDefault(0); // 解除默认
                }
            }
            updateBatchById(list);
        }

        if (isMeDef){
            // 设置为默认
            new LambdaUpdateChainWrapper<>(getBaseMapper())
                    .set(UserAddressBook::getIsDefault, 1)
                    .eq(UserAddressBook::getId, id)
                    .eq(UserAddressBook::getIsDelete, 0)
                    .update();
        }
    }

    @Override
    public void addAddress(UserAddressBook addressBook) {
        Long userId = ((User)ThreadLocalCache.getNotNull()).getId();
        addressBook.setUserId(userId);
        addressBook.setIsDelete(0);

        // 检查是否有要设置为默认地址
        Integer isDefault = addressBook.getIsDefault();
        if (null != isDefault && isDefault > 0){
            // 移除其他的默认地址, 都设置为不是默认地址
            new LambdaUpdateChainWrapper<>(getBaseMapper())
                    .eq(UserAddressBook::getUserId, userId)
                    .eq(UserAddressBook::getIsDefault, 1)
                    .eq(UserAddressBook::getIsDelete, 0)
                    .set(UserAddressBook::getIsDefault, 0)
                    .update();
        }

        // 保存到数据库
        if (!save(addressBook)) throw new InternalServerErrorException(UserStatus.NEW_ADDRESS_ERROR);
    }

    @Override
    @Transactional
    public void updateAddressById(UserAddressBook userAddressBook) {
        if (!updateById(userAddressBook)) throw new BadRequestException(UserStatus.UPDATE_ADDRESS_ERROR);
        Integer isDefault = userAddressBook.getIsDefault();
        if (null != isDefault && isDefault == 1){
            reviseDefault(userAddressBook.getId());// 更新为默认地址
        }
    }

}
