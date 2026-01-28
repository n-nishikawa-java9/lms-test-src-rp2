package jp.co.sss.lms.ct.f02_faq;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.util.List;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト よくある質問機能
 * ケース05
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース05 キーワード検索 正常系")
public class Case05 {

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
		});
	}

	@Test
	@Order(2)
	@DisplayName("テスト02 初回ログイン済みの受講生ユーザーでログイン")
	void test02() {
		// ログインIDとパスワードを入力
		WebElement userId = WebDriverUtils.webDriver.findElement(By.name("loginId"));
		WebElement passWord = WebDriverUtils.webDriver.findElement(By.name("password"));
		userId.sendKeys("StudentAA01");
		passWord.sendKeys("PasswordAA01");

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "01");

		//「ログイン」ボタンをクリックする
		WebDriverUtils.webDriver.findElement(By.className("btn-primary")).click();

		//コース詳細画面表示の待ち時間
		WebDriverUtils.visibilityTimeout(By.className("active"), 5);

		//画面遷移確認
		WebElement errormessages = WebDriverUtils.webDriver.findElement(By.className("active"));
		assertEquals("コース詳細", errormessages.getText());

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(3)
	@DisplayName("テスト03 上部メニューの「ヘルプ」リンクからヘルプ画面に遷移")
	void test03() {
		//ヘッダーの「機能」を押下し、ドロップダウンを表示
		WebDriverUtils.webDriver.findElement(By.className("dropdown")).click();

		//ドロップダウン表示タイムアウト設定
		visibilityTimeout(By.linkText("ヘルプ"), 5);

		//ドロップダウンの表示を確認
		WebElement helpLink = WebDriverUtils.webDriver.findElement(By.linkText("ヘルプ"));
		assertEquals("ヘルプ", helpLink.getText());

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "01");

		//「ヘルプ」リンクを押下
		WebDriverUtils.webDriver.findElement(By.linkText("ヘルプ")).click();
		pageLoadTimeout(5);

		//ヘルプ画面への画面遷移確認
		WebElement helpWebElement = WebDriverUtils.webDriver.findElement(By.tagName("h2"));
		assertEquals("ヘルプ", helpWebElement.getText());

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「よくある質問」リンクからよくある質問画面を別タブに開く")
	void test04() {
		//ヘルプ画面の「よくある質問」リンクをクリック
		WebDriverUtils.webDriver.findElement(By.linkText("よくある質問")).click();
		pageLoadTimeout(5);
		String currHandle = WebDriverUtils.webDriver.getWindowHandle();
		assertNotNull(currHandle);

		//タブの切り替え
		Object[] windowHandles = WebDriverUtils.webDriver.getWindowHandles().toArray();
		WebDriverUtils.webDriver.switchTo().window((String) windowHandles[1]);

		//よくある質問画面への画面遷移確認
		WebElement faqElement = WebDriverUtils.webDriver.findElement(By.tagName("h2"));
		assertEquals("よくある質問", faqElement.getText());

		//エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 キーワード検索で該当キーワードを含む検索結果だけ表示")
	void test05() {
		//よくある質問画面のキーワード検索にキーワードを入力する
		WebDriverUtils.webDriver.findElement(By.name("keyword")).sendKeys("研修");

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "01");

		//検索ボタンを押下する
		WebDriverUtils.webDriver.findElement(By.cssSelector("input[type='submit']")).click();

		//表示の待機時間
		By questionText = By.cssSelector("dt.mb10 span:nth-of-type(2)");
		visibilityTimeout(questionText, 5);

		//検索結果が期待値と一致することを確認
		List<WebElement> qestionTextList = WebDriverUtils.webDriver.findElements(questionText);
		assertEquals(2, qestionTextList.size());
		assertEquals("助成金書類の作成方法が分かりません", qestionTextList.get(0).getText());
		assertEquals("研修の申し込みはどのようにすれば良いですか？", qestionTextList.get(1).getText());

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "02");

	}

	@Test
	@Order(6)
	@DisplayName("テスト06 「クリア」ボタン押下で入力したキーワードを消去")
	void test06() {
		//クリアボタン押下前の入力フォームの状態を確認
		String formTextBefore = WebDriverUtils.webDriver.findElement(By.name("keyword")).getAttribute("value");
		assertEquals("研修", formTextBefore);

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "01");

		//クリアボタンを押下する
		WebDriverUtils.webDriver.findElement(By.cssSelector("input[type='button']")).click();

		//フォームが空になるまで待機
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		wait.until(ExpectedConditions.attributeToBe(By.name("keyword"), "value", ""));

		//入力フォームの中身が消えていることを確認
		String formTextAfter = WebDriverUtils.webDriver.findElement(By.name("keyword")).getAttribute("value");
		assertEquals("", formTextAfter);

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "02");
	}

}
