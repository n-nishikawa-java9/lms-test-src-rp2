package jp.co.sss.lms.ct.f01_login1;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト ログイン機能①
 * ケース02
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース02 受講生 ログイン 認証失敗")
public class Case02 {

	/** 前処理 */
	@BeforeAll
	static void before() {
		createDriver();
	}

	/** 後処理 */
	@AfterAll
	static void after() {
		closeDriver();
	}

	@Test
	@Order(1)
	@DisplayName("テスト01 トップページURLでアクセス")
	void test01() {
		// ログイン画面へ遷移
		goTo("http://localhost:8080/lms");

		//画面遷移確認
		String title = webDriver.getTitle();
		assertEquals("ログイン | LMS", title);

		// エビデンスを取得する
		getEvidence(new Object() {
		}, "01");
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 DBに登録されていないユーザーでログイン")
	void test02() {
		// ログインIDとパスワードを入力
		WebElement userId = WebDriverUtils.webDriver.findElement(By.name("loginId"));
		WebElement passWord = WebDriverUtils.webDriver.findElement(By.name("password"));
		userId.sendKeys("WrongID01");
		passWord.sendKeys("WrongPass01");

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "02");

		//「ログイン」ボタンをクリックする
		WebDriverUtils.webDriver.findElement(By.className("btn-primary")).click();

		//エラーメッセージ表示の待機時間
		WebDriverUtils.visibilityTimeout(By.className("error"), 5);

		//ログイン失敗のエラーメッセージ表示確認
		WebElement errormessages = WebDriverUtils.webDriver.findElement(By.className("error"));
		assertEquals("* ログインに失敗しました。", errormessages.getText());

		getEvidence(new Object() {
		}, "03");

	}

}
