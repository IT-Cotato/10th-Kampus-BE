// package com.cotato.kampus.domain.admin.application;
//
// import static org.junit.jupiter.api.Assertions.*;
// import static org.mockito.Mockito.*;
//
// import java.util.List;
//
// import org.junit.jupiter.api.DisplayName;
// import org.junit.jupiter.api.Test;
// import org.junit.jupiter.api.extension.ExtendWith;
// import org.mockito.InjectMocks;
// import org.mockito.Mock;
// import org.mockito.junit.jupiter.MockitoExtension;
//
// import com.cotato.kampus.domain.board.implement.board.BoardAppender;
// import com.cotato.kampus.domain.board.implement.board.BoardValidator;
// import com.cotato.kampus.domain.board.implement.boardCategory.BoardCategoryAppender;
// import com.cotato.kampus.domain.university.application.UnivFinder;
// import com.cotato.kampus.domain.user.application.UserValidator;
//
// @ExtendWith(MockitoExtension.class)
// class AdminServiceTest {
//
// 	@Mock
// 	private UserValidator userValidator;
//
// 	@Mock
// 	private BoardValidator boardValidator;
//
// 	@Mock
// 	private UnivFinder univFinder;
//
// 	@Mock
// 	private BoardAppender boardAppender;
//
// 	@Mock
// 	private BoardCategoryAppender boardCategoryAppender;
//
// 	@InjectMocks
// 	private AdminService adminService;
//
// 	@Test
// 	@DisplayName("일반 게시판을 카테고리와 함께 생성")
// 	void createGeneralBoardWithCategories() {
// 		// Arrange
// 		String boardName = "자유게시판";
// 		String description = "자유롭게 대화를 나눌 수 있는 게시판입니다.";
// 		String universityCode = null;
// 		List<String> categories = List.of("일상", "질문", "정보");
//
// 		Long expectedBoardId = 1L;
// 		when(boardAppender.appendBoard(boardName,description, null, true))
// 			.thenReturn(1L);
//
// 		// Act
// 		Long boardId = adminService.createBoard(boardName, description, universityCode, categories);
//
// 		// Assert
// 		assertEquals(expectedBoardId, boardId);
// 		verify(userValidator).validateAdminAccess();
// 		verify(boardValidator).validateUniqueName(boardName);
// 		verify(boardAppender).appendBoard(boardName, description, null, true);
// 		verify(boardCategoryAppender).appendCategories(expectedBoardId, categories);
// 	}
//
// 	@Test
// 	@DisplayName("학교 게시판을 카테고리 없이 생성")
// 	void createUniversityBoardWithoutCategories() {
// 		// Arrange
// 		String boardName = "학교 공지사항";
// 		String description = "학교 공지사항을 확인하세요.";
// 		String universityCode = "Hongik University";
// 		List<String> categories = List.of();
//
// 		Long universityId = 401L;
// 		Long expectedBoardId = 1L;
//
// 		when(univFinder.findIdByCode(universityCode)).thenReturn(universityId);
// 		when(boardAppender.appendBoard(boardName, description, universityId, false))
// 			.thenReturn(expectedBoardId);
//
// 		// Act
// 		Long boardId = adminService.createBoard(boardName, description, universityCode, categories);
//
// 		// Assert
// 		assertEquals(expectedBoardId, boardId);
// 		verify(userValidator).validateAdminAccess();
// 		verify(boardValidator).validateUniqueName(boardName);
// 		verify(univFinder).findIdByCode(universityCode);
// 		verify(boardValidator).validateUniversityBoardExists(universityId);
// 		verify(boardAppender).appendBoard(boardName, description, universityId, false);
// 		verify(boardCategoryAppender).appendCategories(expectedBoardId, categories);
// 	}
// }