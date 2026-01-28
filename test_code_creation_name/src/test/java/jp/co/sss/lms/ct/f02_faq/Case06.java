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
 * ケース06
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース06 カテゴリ検索 正常系")
public class Case06 {

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
	@DisplayName("テスト05 カテゴリ検索で該当カテゴリの検索結果だけ表示")
	void test05() {
		//エビデンスを取得する
		getEvidence(new Object() {
		}, "01");

		//よくある質問画面のカテゴリー検索欄にて【研修関係】リンクを押下する。
		WebDriverUtils.webDriver.findElement(By.linkText("【研修関係】")).click();

		//表示の待機時間
		By questionText = By.cssSelector("dt.mb10 span:nth-of-type(2)");
		visibilityTimeout(questionText, 5);

		//検索結果が期待値と一致することを確認
		List<WebElement> qestionTextList = WebDriverUtils.webDriver.findElements(questionText);
		assertEquals(2, qestionTextList.size());
		assertEquals("キャンセル料・途中退校について", qestionTextList.get(0).getText());
		assertEquals("研修の申し込みはどのようにすれば良いですか？", qestionTextList.get(1).getText());

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "02");
	}

	@Test
	@Order(6)
	@DisplayName("テスト06 検索結果の質問をクリックしその回答を表示")
	void test06() {
		WebDriverWait wait = new WebDriverWait(webDriver, Duration.ofSeconds(5));
		//確認するテキスト
		String ansTextCancel = "受講者の退職や解雇等、やむを得ない事情による途中終了に関してなど、"
				+ "事情をお伺いした上で、協議という形を取らせて頂きます。 弊社営業担当までご相談下さい。";
		String ansTextSignUp = "営業担当がいる場合は、営業担当までご連絡ください。 申し込み方法について"
				+ "ご案内させていただきます。 なお、弊社営業営業がいない場合は、東京ITスクール運営事務局まで"
				+ "ご連絡いただけると幸いです。";
		//検索結果の質問をクリック
		for (int i = 0; i < 2; i++) {
			//検索結果の質問を取得する
			List<WebElement> questions = WebDriverUtils.webDriver
					.findElements(By.cssSelector("dl[id^='question-h'] dt.mb10"));
			WebElement question = questions.get(i);

			//クリックできることを確認
			wait.until(ExpectedConditions.elementToBeClickable(question));

			//クリックする
			question.click();

			//表示位置を下げる
			scrollBy("400");

		}

		//検索結果の回答を取得し、正しく表示されているか確認する
		List<WebElement> answers = WebDriverUtils.webDriver.findElements(By.cssSelector("dd.fs18 span:nth-of-type(2)"));
		assertEquals(2, answers.size());
		assertEquals(ansTextCancel, answers.get(0).getText());
		assertEquals(ansTextSignUp, answers.get(1).getText());

		//エビデンスを取得する
		getEvidence(new Object() {
		});
	}

}
