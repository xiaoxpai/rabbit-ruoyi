package com.ruoyi.system.service.impl;

import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.Ztree;
import com.ruoyi.common.core.domain.entity.SysMenu;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysMenuMapper;
import com.ruoyi.system.mapper.SysRoleMenuMapper;
import com.ruoyi.system.service.ISysMenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.text.MessageFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 菜单 业务层处理
 * 
 * @author ruoyi
 */
@Service
public class SysMenuServiceImpl implements ISysMenuService
{
    public static final String PREMISSION_STRING = "perms[\"{0}\"]";

    @Autowired
    private SysMenuMapper menuMapper;

    @Autowired
    private SysRoleMenuMapper roleMenuMapper;

    /**
     * 根据用户查询菜单
     * 
     * @param user 用户信息
     * @return 菜单列表
     */
    @Override
    public List<SysMenu> selectMenusByUser(SysUser user)
    {
        List<SysMenu> menus = new LinkedList<SysMenu>();
        // 管理员显示所有菜单信息
        if (user.isAdmin())
        {
            menus = menuMapper.selectMenuNormalAll();
        }
        else
        {
            menus = menuMapper.selectMenusByUserId(user.getUserId());
        }
        return getChildPerms(menus, 0);
    }

    /**
     * 查询菜单集合
     * 
     * @return 所有菜单信息
     */
    @Override
    public List<SysMenu> selectMenuList(SysMenu menu, Long userId)
    {
        List<SysMenu> menuList = null;
        //校验是否Admin管理员用户
        if (SysUser.isAdmin(userId))
        {
            menuList = menuMapper.selectMenuList(menu);
        }
        else
        {
            menu.getParams().put("userId", userId);
            menuList = menuMapper.selectMenuListByUserId(menu);
        }
        return menuList;
    }

    /**
     * 查询菜单集合
     * 
     * @return 所有菜单信息
     */
    @Override
    public List<SysMenu> selectMenuAll(Long userId)
    {
        List<SysMenu> menuList = null;
        if (SysUser.isAdmin(userId))
        {
            menuList = menuMapper.selectMenuAll();
        }
        else
        {
            menuList = menuMapper.selectMenuAllByUserId(userId);
        }
        return menuList;
    }

    /**
     * 根据用户ID查询权限
     * 
     * @param userId 用户ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectPermsByUserId(Long userId)
    {
        List<String> perms = menuMapper.selectPermsByUserId(userId);
        Set<String> permsSet = new HashSet<>();
        perms.stream().filter(StringUtils::isNotEmpty)
                .forEach(perm -> permsSet.addAll(Arrays.asList(perm.trim().split(","))));
//        for (String perm : perms)
//        {
//            if (StringUtils.isNotEmpty(perm))
//            {
//                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
//            }
//        }
        return permsSet;
    }

    /**
     * 根据角色ID查询权限
     * 
     * @param roleId 角色ID
     * @return 权限列表
     */
    @Override
    public Set<String> selectPermsByRoleId(Long roleId)
    {
        List<String> perms = menuMapper.selectPermsByRoleId(roleId);
        Set<String> permsSet = new HashSet<>();
        perms.stream().filter(StringUtils::isNotEmpty)
                .forEach(perm -> permsSet.addAll(Arrays.asList(perm.trim().split(","))));
//        for (String perm : perms)
//        {
//            if (StringUtils.isNotEmpty(perm))
//            {
//                permsSet.addAll(Arrays.asList(perm.trim().split(",")));
//            }
//        }
        return permsSet;
    }

    /**
     * 根据角色ID查询菜单
     * 
     * @param role 角色对象
     * @return 菜单列表
     */
    @Override
    public List<Ztree> roleMenuTreeData(SysRole role, Long userId)
    {
        Long roleId = role.getRoleId();
        List<Ztree> ztrees = new ArrayList<Ztree>();
        List<SysMenu> menuList = selectMenuAll(userId);
        if (StringUtils.isNotNull(roleId))
        {
            List<String> roleMenuList = menuMapper.selectMenuTree(roleId);
            ztrees = initZtree(menuList, roleMenuList, true);
        }
        else
        {
            ztrees = initZtree(menuList, null, true);
        }
        return ztrees;
    }

    /**
     * 查询所有菜单
     * 
     * @return 菜单列表
     */
    @Override
    public List<Ztree> menuTreeData(Long userId)
    {
        //查询所有菜单树列表
        List<SysMenu> menuList = selectMenuAll(userId);
        //初始化构建树
        List<Ztree> ztrees = initZtree(menuList);
        return ztrees;
    }

    /**
     * 查询系统所有权限
     * 
     * @return 权限列表
     */
    @Override
    public LinkedHashMap<String, String> selectPermsAll(Long userId)
    {
        LinkedHashMap<String, String> section = new LinkedHashMap<>();
        List<SysMenu> permissions = selectMenuAll(userId);
        //使用lambda表达式
        permissions.stream().map(SysMenu::getPerms)
                .filter(StringUtils::isNotEmpty)
                .forEach(perm -> section.put(perm, MessageFormat.format(PREMISSION_STRING, perm)));
//        if (StringUtils.isNotEmpty(permissions))
//        {

//            for (SysMenu menu : permissions)
//            {
//                section.put(menu.getUrl(), MessageFormat.format(PREMISSION_STRING, menu.getPerms()));
//            }
//        }
        return section;
    }

    /**
     * 对象转菜单树
     * 
     * @param menuList 菜单列表
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysMenu> menuList)
    {
        return initZtree(menuList, null, false);
    }

    /**
     * 对象转菜单树
     * 
     * @param menuList 菜单列表
     * @param roleMenuList 角色已存在菜单列表
     * @param permsFlag 是否需要显示权限标识
     * @return 树结构列表
     */
    public List<Ztree> initZtree(List<SysMenu> menuList, List<String> roleMenuList, boolean permsFlag)
    {
        //
        List<Ztree> ztrees = new ArrayList<Ztree>();
        boolean isCheck = StringUtils.isNotNull(roleMenuList);
        for (SysMenu menu : menuList)
        {
            Ztree ztree = new Ztree();
            ztree.setId(menu.getMenuId());
            ztree.setpId(menu.getParentId());
            ztree.setName(transMenuName(menu, permsFlag));
            ztree.setTitle(menu.getMenuName());
            if (isCheck)
            {
                ztree.setChecked(roleMenuList.contains(menu.getMenuId() + menu.getPerms()));
            }
            ztrees.add(ztree);
        }
        return ztrees;
    }

    public String transMenuName(SysMenu menu, boolean permsFlag)
    {
        StringBuffer sb = new StringBuffer();
        sb.append(menu.getMenuName());
        if (permsFlag)
        {
            sb.append("<font color=\"#888\">&nbsp;&nbsp;&nbsp;" + menu.getPerms() + "</font>");
        }
        return sb.toString();
    }

    /**
     * 删除菜单管理信息
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int deleteMenuById(Long menuId)
    {
        return menuMapper.deleteMenuById(menuId);
    }

    /**
     * 根据菜单ID查询信息
     * 
     * @param menuId 菜单ID
     * @return 菜单信息
     */
    @Override
    public SysMenu selectMenuById(Long menuId)
    {
        return menuMapper.selectMenuById(menuId);
    }

    /**
     * 查询子菜单数量
     * 
     * @param parentId 父级菜单ID
     * @return 结果
     */
    @Override
    public int selectCountMenuByParentId(Long parentId)
    {
        return menuMapper.selectCountMenuByParentId(parentId);
    }

    /**
     * 查询菜单使用数量
     * 
     * @param menuId 菜单ID
     * @return 结果
     */
    @Override
    public int selectCountRoleMenuByMenuId(Long menuId)
    {
        return roleMenuMapper.selectCountRoleMenuByMenuId(menuId);
    }

    /**
     * 新增保存菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int insertMenu(SysMenu menu)
    {
        return menuMapper.insertMenu(menu);
    }

    /**
     * 修改保存菜单信息
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public int updateMenu(SysMenu menu)
    {
        return menuMapper.updateMenu(menu);
    }

    /**
     * 校验菜单名称是否唯一
     * 
     * @param menu 菜单信息
     * @return 结果
     */
    @Override
    public String checkMenuNameUnique(SysMenu menu)
    {
        Long menuId = StringUtils.isNull(menu.getMenuId()) ? -1L : menu.getMenuId();
        SysMenu info = menuMapper.checkMenuNameUnique(menu.getMenuName(), menu.getParentId());
        if (StringUtils.isNotNull(info) && info.getMenuId().longValue() != menuId.longValue())
        {
            return UserConstants.MENU_NAME_NOT_UNIQUE;
        }
        return UserConstants.MENU_NAME_UNIQUE;
    }

    /**
     * 查询菜单集合
     * @param menu 菜单信息
     * @param userId 用户ID
     * @return List<RespSystemMenuTreeDTO> 菜单集合
     */
//    @Override
//    public List<RespSystemMenuTreeDTO> selectMenuTree(SysMenu menu, Long userId) {
//        List<RespSystemMenuTreeDTO> menuList = menuMapper.queryMenuList(menu);
//
//        return buildMenuTree(menuList);
//    }

//    private List<RespSystemMenuTreeDTO> buildMenuTree(List<RespSystemMenuTreeDTO> menuList) {
//        //创建一个存放根节点的集合
//        List<RespSystemMenuTreeDTO> result = new ArrayList<>();
//        //获取所有主键id的集合
//        List<Long> ids = menuList.stream()
//                .map(RespSystemMenuTreeDTO::getMenuId)
//                .collect(Collectors.toList());
//        //遍历所有的菜单集合
//        for (RespSystemMenuTreeDTO menu : menuList) {
//            //如果当前菜单的父id不存在ids集合中，则说明当前菜单为根节点
//            if (!ids.contains(menu.getParentId())) {
//                //递归获取当前菜单的子菜单集合
//                recursionFnMenu(menuList, menu);
//                //将当前菜单添加到根节点集合中
//                result.add(menu);
//            }
//        }
//        return result;
//    }

//    private void recursionFnMenu(List<RespSystemMenuTreeDTO> menuList, RespSystemMenuTreeDTO menu) {
//        //获取子节点列表
//        List<RespSystemMenuTreeDTO> childList = getMenuChildList(menuList, menu);
//        menu.setChildren(childList);
//        //遍历子节点列表
//        for (RespSystemMenuTreeDTO child : childList) {
//            //如果子节点的父id存在ids集合中，则递归获取子节点的子节点集合
//            if (getMenuChildList(menuList, child).size() > 0) {
//                //如果子节点还有子节点，就递归
//                recursionFnMenu(menuList, child);
//            }
//        }
//    }

//    private List<RespSystemMenuTreeDTO> getMenuChildList(List<RespSystemMenuTreeDTO> menuList, RespSystemMenuTreeDTO menu) {
//        return menuList.stream()
//                .filter(m -> m.getParentId().equals(menu.getMenuId()))
//                .collect(Collectors.toList());
//    }

    /**
     * 根据父节点的ID获取所有子节点
     * 
     * @param list 分类表
     * @param parentId 传入的父节点ID
     * @return String
     */
    public List<SysMenu> getChildPerms(List<SysMenu> list, int parentId)
    {
        List<SysMenu> returnList = new ArrayList<SysMenu>();
        for (Iterator<SysMenu> iterator = list.iterator(); iterator.hasNext();)
        {
            SysMenu t = (SysMenu) iterator.next();
            // 一、根据传入的某个父节点ID,遍历该父节点的所有子节点
            if (t.getParentId() == parentId)
            {
                recursionFn(list, t);
                returnList.add(t);
            }
        }
        return returnList;
    }

    /**
     * 递归列表
     * 
     * @param list
     * @param t
     */
    private void recursionFn(List<SysMenu> list, SysMenu t)
    {
        // 得到子节点列表
        List<SysMenu> childList = getChildList(list, t);
        t.setChildren(childList);
        for (SysMenu tChild : childList)
        {
            if (hasChild(list, tChild))
            {
                recursionFn(list, tChild);
            }
        }
    }

    /**
     * 得到子节点列表
     */
    private List<SysMenu> getChildList(List<SysMenu> list, SysMenu t)
    {
        List<SysMenu> tlist = new ArrayList<SysMenu>();
        list.stream().filter(n -> n.getParentId().longValue() == t.getMenuId().longValue())
                .forEach(n -> tlist.add(n));
//        Iterator<SysMenu> it = list.iterator();
//        while (it.hasNext())
//        {
//            SysMenu n = (SysMenu) it.next();
//            if (n.getParentId().longValue() == t.getMenuId().longValue())
//            {
//                tlist.add(n);
//            }
//        }
        return tlist;
    }

    /**
     * 判断是否有子节点
     */
    private boolean hasChild(List<SysMenu> list, SysMenu t)
    {
        return getChildList(list, t).size() > 0;
    }
}
