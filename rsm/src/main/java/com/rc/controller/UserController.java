package com.rc.controller;


import cn.hutool.core.bean.BeanUtil;
import com.rc.domain.dto.*;
import com.rc.domain.entity.RsmUser;
import com.rc.domain.entity.User;
import com.rc.service.IUserService;
import com.rc.utils.UserHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;




/**
 * <p>
 * 前端控制器
 * </p>
 *
 * @author 虎哥
 * @since 2021-12-22
 */
@Slf4j
@Api(tags = "用户登入系统")
@RestController
@RequestMapping("/api/mp")
public class UserController {
    @Resource
    private IUserService userService;

    /**
     * 登出功能
     * @return 无
     */
    @ApiOperation(value = "退出登入")
    @PostMapping("/login/exit")
    public Result logout(@RequestHeader("Authorization") String authorization) {
        return userService.logout(authorization);
    }


    @ApiOperation(value = "微信一键登入")
    @PostMapping("/login/weixin")
    public Result login(@RequestBody String code, HttpSession session){
        return userService.login(code,session);
    }


    /**
      * 登录功能
      * @param  ，包含手机号、验证码；或者手机号、密码
     *
     */
    @ApiOperation(value = "绑定员工账号并登入")
    @PostMapping("/bindEmployeeAccount")
    public Result bandWithPasswd(@RequestBody LoginFormDTO loginForm, HttpSession session) throws Exception {
        //实现登入功能
        return userService.bandWithPasswd(loginForm,session);
    }

    @ApiOperation("获取我的用户信息")
    @GetMapping("/me")
    public Result me() {
        // 获取当前登录的用户并返回
        UserDTO user = UserHolder.getUser();
        return Result.ok(user);
    }

    @ApiOperation(value = "根据ID获取用户信息")
    @GetMapping("/users/{id}")
    public Result queryUserById(@PathVariable("id") Long id) {
        return userService.queryUserById(id);
    }

    @ApiOperation(value = "更新我的用户信息")
    @PutMapping("/me")
    public Result updateUser(@RequestBody User user) {

        // 返回用户信息
        return userService.userUpdateById(user);
    }

    @ApiOperation(value = "模糊搜索用户列表")
    @GetMapping("/users/search")
    public Result queryUserByKeyword(
            @RequestParam(value = "keyword",required = false) String keyword
    ){
        return userService.queryUserByKeyword(keyword);
    }





    /**
    * 登录功能
    * @param  ，包含手机号、验证码；或者手机号、密码
    */
    @ApiOperation(value = "验证验证码")
    @PostMapping("/smscode/verify")
    public Result verifyCode(@RequestBody PhoneDTO phoneDTO, HttpSession session){
        //实现登入功能
        return userService.verifyCode(phoneDTO,session);
    }

    /**
     * 发送手机验证码
     */
    @ApiOperation(value = "发送验证码")
    @PostMapping("/smscode/send")
    public Result sendCode(@RequestBody PhoneDTO phoneDTO, HttpSession session) {
        log.info("发送验证码短信验证码，手机号：{}", phoneDTO.getPhoneNumber());
        return userService.sendCode(phoneDTO.getPhoneNumber(), session);
    }

    @ApiOperation(value = "修改我的密码")
    @PostMapping("/me/password/change")
    public Result updatePassword(@RequestBody PassWordDTO passWordDTO) throws Exception {
        return userService.updatePassword(passWordDTO);
    }

    @ApiOperation(value = "获取今日代办")
    @GetMapping("/me/todo")
    public Result getTodoList() {
        return userService.getTodoList();
    }
//    /**
//     *
//     * @param id
//     * @return ok
//     */
//    //根据id查用户
//    @ApiOperation(value = "根据id查询用户")
//    @GetMapping("/users/{id}")
//    public Result queryUserById(@PathVariable("id") Long id) {
//        // 查询详情
//        User user = userService.getById(id);
//        if (user == null) {
//            return Result.ok();
//        }
//        UserDTO userDTO = BeanUtil.copyProperties(user, UserDTO.class);
//        // 返回
//        return Result.ok(userDTO);
//    }
//    /**
//     * 发送手机验证码
//     */
//    @ApiOperation(value = "发送验证码")
//    @PostMapping("/smscode/send")
//    public Result sendCode(@RequestBody LoginFormDTO loginFormDTO, HttpSession session) {
//        log.info("发送验证码短信验证码，手机号：{}", loginFormDTO.getPhone());
//        return userService.sendCode(loginFormDTO.getPhone(), session);
//    }

//    /**
//     * 登录功能
//     * @param  ，包含手机号、验证码；或者手机号、密码
//     */
//    @ApiOperation(value = "验证验证码")
//    @PostMapping("/login/phone")
//    public Result bandWithPasswd(@RequestBody String code, HttpSession session){
//        //实现登入功能
//        return userService.bandWithPasswd(code,session);
//    }
//    @ApiOperation(value = "签到")
//    @PostMapping("/sign")
//    public Result sign(){
//        return userService.sign();
//    }

//    @ApiOperation(value = "查询签到次数")
//    @GetMapping("/sign/count")
//    public Result signCount(){
//        return userService.signCount();
//    }


//    @ApiOperation(value = "使用密码登入")
//    @PostMapping("/login/pwd")
//    public Result login2(@RequestBody LoginFormDTO loginForm, HttpSession session){
//        //实现登入功能
//        return userService.loginWithPassword(loginForm,session);
//    }
//
//    @ApiOperation(value = "更改密码")
//    @PostMapping("/users/password")
//    public Result updatePassword(@RequestBody LoginFormDTO loginForm){
//        return userService.updatePassword(loginForm);
//    }

}
