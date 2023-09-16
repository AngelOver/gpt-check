package com.example.chatcheck.other;
import cn.hutool.extra.mail.Mail;
import cn.hutool.extra.mail.MailAccount;

public class WY163MailUtil {

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
        account.setHost("smtp.163.com");
        account.setPort(25);
        account.setAuth(true);
        account.setFrom("a3r_cn@163.com"); // 邮件发送者的地址
        account.setUser("a3r_cn@163.com"); // 邮箱帐号
        account.setPass("NUKMOPTEGYKLIQPS"); // 邮箱授权码

        Mail.create(account)
                .setTos(to)
                .setTitle(subject)
                .setContent(content)
                //.setUseGlobalSession(false) // 关闭共享session
                //.setSslEnable(true) // 开启SSL加密
                .send();


    }

    public static void main(String[] args) {
        try {
            WY163MailUtil.send("2424123787@qq.com", "163测试邮件", "这是一封163测试邮件，请勿回复。");
            System.out.println("邮件发送成功！");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("邮件发送失败：" + e.getMessage());
        }
    }

}
