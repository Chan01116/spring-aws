package com.myaws.myapp.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

public class LoginInterceptor extends HandlerInterceptorAdapter {  //회원정보를 세션에 담는 역활
	
	// 로그인후에 회원정보를 세션에 담는다  핸들러 인터셉터어댑터에 들어가서 아래를 복사
	
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		
		//가로채기 하기전에 처리하는 메서드
		HttpSession session = request.getSession();
		
		if(session.getAttribute("midx")!= null) {
			
			session.removeAttribute("midx");
			session.removeAttribute("memberId");
			session.removeAttribute("memberName");
			session.invalidate();  //초기화 시킨다
			
		}
		

		return true;
	}

	
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception { //모델앤뷰에서 담았던 객체 꺼내기
		
		
		//RedirectAttributes나 Model객체에 담은 값을 꺼낸다
		String midx = modelAndView.getModel().get("midx").toString();
		String memberName = modelAndView.getModel().get("memberName").toString();
		String memberId = modelAndView.getModel().get("memberId").toString();
		
		modelAndView.getModel().clear(); //파라미터 model값을 지운다
		
		
		HttpSession session = request.getSession();
		if(midx != null) {
			session.setAttribute("midx", midx);
			session.setAttribute("memberId", memberId);
			session.setAttribute("memberName", memberName);			
		}		
		
		
	}

	
	

}
