package jp.co.sss.lms.ct.f03_report;

import static jp.co.sss.lms.ct.util.WebDriverUtils.*;
import static org.junit.jupiter.api.Assertions.*;

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

import jp.co.sss.lms.ct.util.WebDriverUtils;

/**
 * 結合テスト レポート機能
 * ケース07
 * @author holy
 */
@TestMethodOrder(OrderAnnotation.class)
@DisplayName("ケース07 受講生 レポート新規登録(日報) 正常系")
public class Case07 {

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
	@DisplayName("テスト03 未提出の研修日の「詳細」ボタンを押下しセクション詳細画面に遷移")
	void test03() {
		//「提出」「未提出」リスト
		List<WebElement> courseList = WebDriverUtils.webDriver
				.findElements(By.cssSelector("table.table td:nth-of-type(3)"));
		//「詳細」ボタンリスト
		List<WebElement> detailList = WebDriverUtils.webDriver.findElements(By.cssSelector("td.w20per input.btn"));

		int listNum = 0;
		//「未提出」の項目を探す
		for (WebElement course : courseList) {
			String text = "未提出";

			if (course.getText().equals(text)) {
				break;
			}

			++listNum;
		}

		//押下する対象が「未提出」
		assertEquals("未提出", courseList.get(listNum).getText());

		//「詳細」ボタンを押下する
		detailList.get(listNum).click();

		//明示的待機
		visibilityTimeout(By.className("active"), 5);

		//遷移確認
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		//エビデンスを取得する
		getEvidence(new Object() {
		});

	}

	@Test
	@Order(4)
	@DisplayName("テスト04 「提出する」ボタンを押下しレポート登録画面に遷移")
	void test04() {
		WebDriverUtils.webDriver.findElement(By.cssSelector("form[action*='/report/regist'] input[type='submit'].btn"))
				.click();

		visibilityTimeout(By.cssSelector("div.bs-component legend"), 5);

		assertEquals("報告レポート",
				WebDriverUtils.webDriver.findElement(By.cssSelector("div.bs-component legend")).getText());

		//エビデンスを取得する
		getEvidence(new Object() {
		});
	}

	@Test
	@Order(5)
	@DisplayName("テスト05 報告内容を入力して「提出する」ボタンを押下し確認ボタン名が更新される")
	void test05() {
		// 報告内容を入力
		WebDriverUtils.webDriver.findElement(By.cssSelector("div.well.bs-component textarea.form-control"))
				.sendKeys("レポートを登録します。");

		//エビデンスを取得する
		getEvidence(new Object() {
		}, "01");

		//「提出する」ボタンを押下する。
		WebDriverUtils.webDriver.findElement(By.cssSelector("div.form-group button.btn-primary")).click();
		visibilityTimeout(By.className("active"), 5);

		//遷移確認
		assertEquals("セクション詳細 | LMS", webDriver.getTitle());

		By buttonLocation = By.cssSelector("form[action*='/report/regist'] input[type='submit'].btn");
		String buttonTitle = WebDriverUtils.webDriver.findElement(buttonLocation).getAttribute("value");
		assertEquals("提出済み日報【デモ】を確認する", buttonTitle);

		getEvidence(new Object() {
		}, "02");

	}

}
