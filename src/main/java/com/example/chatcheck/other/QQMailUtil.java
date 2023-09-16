package com.example.chatcheck.other;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;

public class QQMailUtil {

    /**
     * 发送邮件
     *
     * @param to      收件人的邮箱地址
     * @param subject 邮件主题
     * @param content 邮件内容
     * @throws Exception 异常
     */
    public static void send(String to, String subject, String content) throws Exception {
        MailAccount account = new MailAccount();
        account.setHost("smtp.qq.com");
        account.setPort(587);
        account.setAuth(true);
        account.setFrom("2424123787@qq.com"); // 邮件发送者的地址
        account.setUser("2424123787@qq.com"); // 邮箱帐号
        account.setPass("hpycqnivahffdiaj"); // 邮箱授权码
        Mail.create(account)
                .setTos(to)
                .setTitle(subject)
                .setContent(content)
                .setUseGlobalSession(false) // 关闭共享session
                //.setSslEnable(true) // 开启SSL加密
                .send();
    }

    public static void main(String[] args) {
        try {
            QQMailUtil.send("1243076446@qq.com", "测试邮件", "这是一封测试邮件，请勿回复。");
            System.out.println("邮件发送成功！");
        } catch (Exception e) {
            System.err.println("邮件发送失败：" + e.getMessage());
        }
    }

}
