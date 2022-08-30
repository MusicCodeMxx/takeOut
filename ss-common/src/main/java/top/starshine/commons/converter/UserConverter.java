package top.starshine.commons.converter;

import org.mapstruct.InheritConfiguration;
import org.mapstruct.Mapper;
import top.starshine.commons.entity.user.*;

import java.util.List;

/**
 * <h3>用户 Bean 转换器</h3>
 * @author: starshine
 * @version: 1.0
 * @since: 2022/7/24  下午 3:55  周日
 * @Description:
 */
@Mapper(componentModel = "spring")
public interface UserConverter {

    UserVo userToUserVo(User source);

    @InheritConfiguration(name = "userAddressBookToUserAddressBookVo")
    List<UserAddressBookVo> userAddressBooksToUserAddressBookVos(List<UserAddressBook> source);

    UserAddressBookVo userAddressBookToUserAddressBookVo(UserAddressBook source);

    UserAddressBook userAddressBookVcToUserAddressBook(UserAddressBookVc source);

    UserAddressBook userAddressBookUpdateVcToUserAddressBook(UserAddressBookUpdateVc source);

}
