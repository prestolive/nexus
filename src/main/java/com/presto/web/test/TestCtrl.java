package com.presto.web.test;

import java.lang.reflect.Method;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.presto.core.da.BaseDAO;
import com.presto.sys.operate.SysOperateVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.view.RedirectView;

import com.presto.dev.build.ActionBuildField;

/**
 * Created by Administrator on 2017/3/12.
 */
@Transactional //声明式事务支�?
@Controller
public class TestCtrl {

    @Autowired
    private BaseDAO baseDAO;

    @RequestMapping(value = "/testtrans")
    public ModelAndView testTrans() throws Exception {
        SysOperateVO vo =new SysOperateVO();
        vo.setVcode("xxx");
        vo.setVname("xxx");
        vo.setVpassword("xxx");
        vo.setState(1);
        baseDAO.insertOrUpdateValueObject(vo);
        if(1==1){
            throw new RuntimeException("xxx");
        }
        return new ModelAndView("/main/test");
    }

    @ActionBuildField(module="test",app="user",group="list",action="listuser",name="�û��嵥",desc="�����嵥����")
    @RequestMapping(value="/testinsert")
    public ModelAndView testInsert(String name,String code,String password,ModelMap m, HttpServletRequest request, HttpServletResponse response) throws Exception{
        //
        SysOperateVO vo =new SysOperateVO();
        vo.setVcode(code);
        vo.setVname(name);
        vo.setVpassword(password);
        vo.setState(1);
        baseDAO.insertOrUpdateValueObject(vo);
        //
        List<SysOperateVO> list = baseDAO.queryValueObjectForList(SysOperateVO.class);
        m.put("operates",list);
        return new ModelAndView(new RedirectView("./test"));
    }

    @RequestMapping(value = "/testjson")
    public @ResponseBody List<SysOperateVO> getVOs() throws Exception {
        return baseDAO.queryValueObjectForList(SysOperateVO.class);
    }

    @RequestMapping(value="/test")
    public ModelAndView testList(ModelMap m, HttpServletRequest request, HttpServletResponse response) throws Exception{
        List<SysOperateVO> list = baseDAO.queryValueObjectForList(SysOperateVO.class);
        m.put("operates",list);
        return new ModelAndView("/main/test");
    }

    @RequestMapping(value="/testannon")
    public void testannon(ModelMap m, HttpServletRequest request, HttpServletResponse response) throws Exception{
        Method[]  methods= TestCtrl.class.getMethods();
        for(Method method:methods){
            ActionBuildField annon =method.getAnnotation(ActionBuildField.class);
            if(annon!=null){
                System.out.println(annon.module());
                System.out.println(annon.app());
                System.out.println(annon.group());
                System.out.println(annon.action());
                System.out.println(annon.name());
                System.out.println(annon.desc());
            }

        }
    }
	
}
