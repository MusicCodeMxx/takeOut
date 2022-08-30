package top.starshine.controller;

import com.baomidou.mybatisplus.extension.conditions.query.LambdaQueryChainWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import top.starshine.commons.aspect.AesDecrypt;
import top.starshine.commons.aspect.ApiRestController;
import top.starshine.commons.aspect.RedissonLock;
import top.starshine.commons.aspect.ReturnResultToAesEncrypt;
import top.starshine.commons.converter.UserConverter;
import top.starshine.commons.entity.user.*;
import top.starshine.commons.exception.BadRequestException;
import top.starshine.commons.handle.ThreadLocalCache;
import top.starshine.commons.status.UserStatus;
import top.starshine.service.UserAddressBookService;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户地址簿相关的接口
 * <li>获取地址列表</li>
 * <li>添加地址</li>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/6/28  下午 2:38  周二
 * @Description:
 */
@Slf4j
@RequiredArgsConstructor
@ApiRestController("/addressBook/")
public class UserAddressBookController {

    private final UserConverter userConverter;
    private final UserAddressBookService addressBookService;

    // 通过地址簿主键查询地址信息
    @GetMapping("find/by/{id}")
    public UserAddressBookVo findById(@PathVariable("id") Long id) {
        return userConverter.userAddressBookToUserAddressBookVo(addressBookService.getById(id));
    }

    // 删除指定地址
    @DeleteMapping("delete/by/{id}")
    public void delete(@PathVariable("id") Long id) {
        if (!addressBookService.removeById(id)) throw new BadRequestException(UserStatus.ERROR);
    }

    // 修改默认地址
    @PutMapping("reviseDefault/by/{id}")
    public void reviseDefault(@PathVariable("id") Long id){
        addressBookService.reviseDefault(id);
    }

    // 修改地址
    @PutMapping("update")
    public void update(@RequestBody @Validated UserAddressBookUpdateVc sabu){
        addressBookService.updateAddressById(userConverter.userAddressBookUpdateVcToUserAddressBook(sabu));
    }

    // 添加地址, 全部传入加密数据
    @RedissonLock
    @Transactional
    @PostMapping("add")
    public void add(@AesDecrypt({"consigneeName", "phoneNumber", "detail"}) @Validated UserAddressBookVc userAddressBookVc){
        UserAddressBook addressBook = userConverter.userAddressBookVcToUserAddressBook(userAddressBookVc);
        addressBookService.addAddress(addressBook);
    }

    // 获取地址簿
    @GetMapping("list")
    public List<UserAddressBookVo> list(){
        User user = ThreadLocalCache.getNotNull();
        return userConverter.userAddressBooksToUserAddressBookVos(
                new LambdaQueryChainWrapper<>(addressBookService.getBaseMapper())
                        .eq(UserAddressBook::getUserId, user.getId())
                        .eq(UserAddressBook::getIsDelete, 0)
                        .orderByDesc(UserAddressBook::getIsDefault)
                        .select(UserAddressBook::getId,
                                UserAddressBook::getConsigneeName,
                                UserAddressBook::getPhoneNumber,
                                UserAddressBook::getSex,
                                UserAddressBook::getDetail,
                                UserAddressBook::getLabel,
                                UserAddressBook::getIsDefault)
                        .list());
    }

    // 获取默认地址
    @ReturnResultToAesEncrypt({"consigneeName", "phoneNumber", "detail"})
    @GetMapping("find/default")
    public UserAddressBookVo findDefault(){
        return userConverter.userAddressBookToUserAddressBookVo(addressBookService.getDefault());
    }

}
