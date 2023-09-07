package hello.jdbc.exception.translator;

import hello.jdbc.Repository.exception.MyDBDuplicateKeyException;
import hello.jdbc.Repository.exception.MyDBException;
import hello.jdbc.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

import static hello.jdbc.connection.ConnectionConst.*;

@Slf4j
public class ExTranslatorV1Test {

  private Repository repository;
  private Service service;


  @BeforeEach
  void before() {
    DriverManagerDataSource dataSource = new DriverManagerDataSource(URL, USERNAME, PASSWORD);
    repository = new Repository(dataSource);
    service = new Service(repository);
  }


  @DisplayName("DB Key 중복 에러발생 시, Exception 처리")
  @Test
  void duplicateKeySave() {
    // Given
    service.create("myId");
    service.create("myId");

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
