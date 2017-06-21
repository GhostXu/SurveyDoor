package org.apache.struts2.views.jsp.ui;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.jsp.JspException;
import javax.servlet.jsp.tagext.Tag;

import org.apache.struts2.components.Component;
import org.apache.struts2.components.Submit;

import cn.util.ValidateUtil;

import com.opensymphony.xwork2.util.ValueStack;

public class SubmitTag extends AbstractClosingTag{

	private static final long serialVersionUID = 1531062273531785788L;

	protected String action;
    protected String method;
    protected String align;
    protected String type;
    protected String src;
    
	@Override
	public Component getBean(ValueStack arg0, HttpServletRequest arg1,
			HttpServletResponse arg2) {
		return new Submit(arg0, arg1, arg2);
	}


    protected void populateParams() {
        super.populateParams();

        Submit submit = ((Submit) component);
        submit.setAction(action);
        submit.setMethod(method);
        submit.setAlign(align);
        submit.setType(type);
        submit.setSrc(src);
    }

    public void setAction(String action) {
        this.action = action;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public void setAlign(String align) {
        this.align = align;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setSrc(String src) {
        this.src = src;
    }
    
    @Override
	public int doEndTag() throws JspException {
		return hasRight() ? super.doEndTag() : SKIP_BODY;
	}
   
    @Override
	public int doStartTag() throws JspException {
		return hasRight() ? super.doStartTag() : SKIP_BODY;
	}
    
    private boolean hasRight(){
    	return ValidateUtil.hasRight(getFormNamespace(), getFormAction(), (HttpServletRequest)pageContext.getRequest(), null);
    }
    
    private String getFormAction() {
    	Tag pTag = this.getParent();
    	while(pTag != null){
    		if(pTag instanceof FormTag){
    			return ((FormTag)pTag).action ;
    		}else{
    			pTag = pTag.getParent() ;
    		}
    	}
		return null;
	}


	private String getFormNamespace(){
		Tag pTag = this.getParent() ;
		while(pTag != null){
			if(pTag instanceof FormTag){
				return ((FormTag)pTag).namespace ;
			}else{
				pTag = pTag.getParent() ;
			}
		}
    	return "";
    }
}
