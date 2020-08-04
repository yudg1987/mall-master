package com.d2c.admin.config;

import de.codecentric.boot.admin.server.config.AdminServerProperties;

import org.apdplat.word.analysis.SimHashPlusHammingDistanceTextSimilarity;
import org.apdplat.word.analysis.TextSimilarity;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final String adminContextPath;

    public WebSecurityConfig(AdminServerProperties adminServerProperties) {
        this.adminContextPath = adminServerProperties.getContextPath();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        SavedRequestAwareAuthenticationSuccessHandler successHandler = new SavedRequestAwareAuthenticationSuccessHandler();
        successHandler.setTargetUrlParameter("redirectTo");
        http
                .authorizeRequests()
                .antMatchers(adminContextPath + "/assets/**")
                .permitAll()
                .antMatchers(adminContextPath + "/login")
                .permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .formLogin()
                .loginPage(adminContextPath + "/login")
                .successHandler(successHandler)
                .and()
                .logout()
                .logoutUrl(adminContextPath + "/logout")
                .and()
                .httpBasic()
                .and()
                .csrf()
                .disable();
    }
    
  //深圳市宝安区人民法院
    public static void main(String[] args) {
    	String text1 = "我爱购物";
    	String text2 = "我爱读书";
    	String text3 = "他是黑客";
    	
    	String text4 = "广东省深圳市宝安区";
    	String text5 = "深圳市宝安区人民法院";
    	String text6 = "宝安区人民法院";
    	TextSimilarity textSimilarity = new SimHashPlusHammingDistanceTextSimilarity();
    	double score1pk1 = textSimilarity.similarScore(text1, text1);
    	double score1pk2 = textSimilarity.similarScore(text1, text2);
    	double score1pk3 = textSimilarity.similarScore(text1, text3);
    	double score2pk2 = textSimilarity.similarScore(text2, text2);
    	double score2pk3 = textSimilarity.similarScore(text2, text3);
    	double score3pk3 = textSimilarity.similarScore(text3, text3);
    	
    	double score4pk5 = textSimilarity.similarScore(text4, text5);
    	double score4pk6 = textSimilarity.similarScore(text4, text6);
    	double score5pk6 = textSimilarity.similarScore(text4, text6);
    	System.out.println(text1+" 和 "+text1+" 的相似度分值："+score1pk1);
    	System.out.println(text1+" 和 "+text2+" 的相似度分值："+score1pk2);
    	System.out.println(text1+" 和 "+text3+" 的相似度分值："+score1pk3);
    	System.out.println(text2+" 和 "+text2+" 的相似度分值："+score2pk2);
    	System.out.println(text2+" 和 "+text3+" 的相似度分值："+score2pk3);
    	System.out.println(text3+" 和 "+text3+" 的相似度分值："+score3pk3);
    	
    	System.out.println(text4+" 和 "+text5+" 的相似度分值："+score4pk5);
    	System.out.println(text4+" 和 "+text6+" 的相似度分值："+score4pk6);
    	System.out.println(text5+" 和 "+text6+" 的相似度分值："+score5pk6);
	}

}
