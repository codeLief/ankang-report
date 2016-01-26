package com.ankang.report.filet.iml;


import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.ankang.report.annotation.Activate;
import com.ankang.report.filet.Fileter;
import com.ankang.report.filet.Invocation;
import com.ankang.report.filet.Invoker;
import com.ankang.report.model.ReportResponse;

@Activate(order = Integer.MAX_VALUE - 2)
public final class AssembleFileter implements Fileter {

	@Override
	public ReportResponse invoke(Invoker invoker, HttpServletRequest request,
			Invocation invocation) throws Exception {
		
		if(invocation.getParameterTypes().length > 0){
			if(invocation.getArguments() == null || invocation.getArguments().length < 1){
				invocation.setArguments(new Object[invocation.getParameterTypes().length]);
			}
			Object[] invokerParam = invocation.getArguments();
			Class<?>[] parameterTypes = invocation.getParameterTypes();
			for (int i = 0; i < parameterTypes.length; i++) {
				if (HttpServletRequest.class
						.isAssignableFrom(parameterTypes[i])) {
					invokerParam[i] = request;
					continue;
				}else if(Map.class.isAssignableFrom(parameterTypes[i])){
					invokerParam[i] = invocation.getResolver().getParamsMap(request);
				} 
			}
		}
		return invoker.invoke(request, invocation);
	}

}
