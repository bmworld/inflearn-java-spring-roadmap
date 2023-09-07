package hello.jdbc.exception.translator;

import hello.jdbc.Repository.exception.MyDBDuplicateKeyException;
import hello.jdbc.Repository.exception.MyDBException;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.BadSqlGrammarException;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;
import org.springframework.jdbc.support.SQLErrorCodeSQLExceptionTranslator;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static hello.jdbc.connection.ConnectionConst.*;
import static org.assertj.core.api.Assertions.assertThat;


/**
 * <h1>DB Exception 발생 시,  Spring 제공하는 Spring Data 접근 계층의 예외로 변환하여 반환하는 것을 사용할 수 있다.</h1>
 * <pre>
 *   왜 필요한가?
 *   - 각각의 DB마다 ErrorCode가 상이하다.
 *   - 따라서 Spring은 각가의 DB가 제공하는 SQL ErorCode를 제공할 수 있다.
 *
 * </pre>
 * <h1>HOW????</h1>
 * <pre>
 *    - Spring SQL Exception Translator는 아래 파일을 통하여, Spring Data Access Exception을 찾아낸다.
 *
 *
 *
 *   {@code
 *   <bean id="DB2" name="Db2" class="org.springframework.jdbc.support.SQLErrorCodes">
 * 		<property name="databaseProductName">
 * 			<value>DB2*</value>
 * 		</property>
 * 		<property name="badSqlGrammarCodes">
 * 			<value>-007,-029,-097,-104,-109,-115,-128,-199,-204,-206,-301,-408,-441,-491</value>
 * 		</property>
 * 		<property name="duplicateKeyCodes">
 * 			<value>-803</value>
 * 		</property>
 * 		<property name="dataIntegrityViolationCodes">
 * 			<value>-407,-530,-531,-532,-543,-544,-545,-603,-667</value>
 * 		</property>
 * 		<property name="dataAccessResourceFailureCodes">
 * 			<value>-904,-971</value>
 * 		</property>
 * 		<property name="transientDataAccessResourceCodes">
 * 			<value>-1035,-1218,-30080,-30081</value>
 * 		</property>
 * 		<property name="deadlockLoserCodes">
 * 			<value>-911,-913</value>
 * 		</property>
 * 	</bean>
 *  }
 *   ...
 * </pre>
 */
@Slf4j
public class SpringExceptionTranslatorTest {

  private Repository repository;
  private Service service;

  private DataSource dataSource;
  private final int H2_Database_SQLGrammarExceptionCode = 42122;


  @BeforeEach
  void before() {
    dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    repository = new Repository(dataSource);
    service = new Service(repository);
  }


  @DisplayName("Spring Exception Translator 미사용")
  @Test
  void sqlExceptionErrorByInvalidSQLGrammar() {
    // Given
    String sql = "SELECT bad grammar";
    try {

      // When
      Connection con = dataSource.getConnection();
      PreparedStatement pstmt = con.prepareStatement(sql);
      pstmt.executeQuery();

    } catch (SQLException e) {
      // Then
      int errorCode = e.getErrorCode();
      assertThat(errorCode).isEqualTo(H2_Database_SQLGrammarExceptionCode);
      log.info("Exception ErrorCode={}", errorCode);
    }


  }


  @DisplayName("Spring Exception Translator Ver.")
  @Test
  void springExTranslator() {
    // Given
    String sql = "SELECT bad grammar";
    try {

      // When
      Connection con = dataSource.getConnection();
      PreparedStatement pstmt = con.prepareStatement(sql);
      pstmt.executeQuery();

    } catch (SQLException e) {
      // then
      int errorCode = e.getErrorCode();
      assertThat(errorCode).isEqualTo(H2_Database_SQLGrammarExceptionCode);
      log.info("ErrorCode by Manual Ex={}", errorCode);
      SQLErrorCodeSQLExceptionTranslator sqlErrorCodeSQLExceptionTranslator = new SQLErrorCodeSQLExceptionTranslator(dataSource);
      // SQL Ex Translator = 알아서 Exception 분석해서 DATA ex 반환한다.

      DataAccessException resultException = sqlErrorCodeSQLExceptionTranslator.translate("select", sql, e);
      log.info("ErrorCode by SQLErrorCodeSQLExceptionTranslator ={}", resultException);
      assertThat(resultException.getClass()).isEqualTo(BadSqlGrammarException.class);

    }

  }
  /// =================================================================
  /// =================================================================
  /// =================================================================
  /// =================================================================
  /// =================================================================

  @RequiredArgsConstructor
  static class Service {
    private final Repository repository;

    public void create(String memberId) {
      try {
        repository.save(new Member(memberId, 0));
        log.info("--- saved Id={}", memberId);
      } catch (MyDBDuplicateKeyException e) {
        log.info("DB Duplicate key: 키 중복 및 복구 시도");
        String retryId = generateNewMemberId(memberId);
        log.info("retryId ={}", retryId);
        repository.save(new Member(retryId, 0));
      } catch (MyDBException e) {
        log.info("데이터 접근 시도 중 에러 ", e);

      }


    }


    private String generateNewMemberId(String memberId) {
      // DB 내에 column key 중복 시, Random number 붙여서 신규 생성한다 ( 각 회사의 내규에 따라서 알아서 처리하시라)
      return memberId + new Random().nextInt(10000);
    }

  }

  @RequiredArgsConstructor
  static class Repository {
    private final DataSource dataSource;

    public Member save(Member member) {
      String sql = "INSERT INTO member(member_id, money) VALUES (?,?)";
      Connection con = null;
      PreparedStatement pstmt = null;
      try {
        con = dataSource.getConnection();
        pstmt = con.prepareStatement((sql));
        pstmt.setString(1, member.getMemberId());
        pstmt.setInt(2, member.getMoney());
        pstmt.executeUpdate();
        return member;

      } catch (SQLException e) {
        // h2 db
        if (e.getErrorCode() == 23505) {
          throw new MyDBDuplicateKeyException(e);
        }
        throw new MyDBException();

      } finally {

        JdbcUtils.closeStatement(pstmt);
        JdbcUtils.closeConnection(con);

      }

    }

  }
}
