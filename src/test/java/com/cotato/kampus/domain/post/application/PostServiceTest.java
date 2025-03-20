package com.cotato.kampus.domain.post.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import com.cotato.kampus.domain.board.application.BoardFinder;
import com.cotato.kampus.domain.board.application.BoardValidator;
import com.cotato.kampus.domain.board.application.CategoryResolver;
import com.cotato.kampus.domain.board.dto.BoardDto;
import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.domain.comment.application.CommentDeleter;
import com.cotato.kampus.domain.common.application.ApiUserResolver;
import com.cotato.kampus.domain.common.application.ImageValidator;
import com.cotato.kampus.domain.post.dto.PostWithPhotos;
import com.cotato.kampus.domain.post.enums.PostSortType;
import com.cotato.kampus.domain.user.dto.UserDto;
import com.cotato.kampus.domain.user.enums.UserRole;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import com.cotato.kampus.global.error.exception.ImageException;
import com.cotato.kampus.global.util.s3.S3Uploader;
import org.mockito.quality.Strictness;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PostServiceTest {

	@Mock
	private PostAppender postAppender;
	@Mock
	private ApiUserResolver apiUserResolver;
	@Mock
	private BoardFinder boardFinder;
	@Mock
	private BoardValidator boardValidator;
	@Mock
	private ImageValidator imageValidator;
	@Mock
	private S3Uploader s3Uploader;
	@Mock
	private PostPhotoAppender postPhotoAppender;
	@Mock
	private PostPhotoFinder postPhotoFinder;
	@Mock
	private CategoryResolver categoryResolver;
	@Mock
	private PostCategoryAppender postCategoryAppender;
	@Mock
	private PostValidator postValidator;
	@Mock
	private PostPhotoDeleter postPhotoDeleter;
	@Mock
	private PostCategoryDeleter postCategoryDeleter;
	@Mock
	private PostLikeUpdater postLikeUpdater;
	@Mock
	private PostScrapUpdater postScrapUpdater;
	@Mock
	private CommentDeleter commentDeleter;
	@Mock
	private PostDeleter postDeleter;
	@Mock
	private PostFinder postFinder;
	@InjectMocks
	private PostService postService;

	private Long boardId;
	private String title;
	private String content;
	private UserDto verifiedUserDto;
	private UserDto unverifiedUserDto;
	private BoardDto generalBoardDto;
	private BoardDto universityBoardDto;
	private MockMultipartFile imageFile;
	private List<MultipartFile> images;
	private List<String> categories;
	private Long postId;

	@BeforeEach
	void setUp() {
		boardId = 1L;
		title = "테스트 제목";
		content = "테스트 내용";
		postId = 1L;

		// 재학생 인증된 사용자
		UserDto verifiedUserDto = Mockito.mock(UserDto.class);
		when(verifiedUserDto.id()).thenReturn(1L);
		when(verifiedUserDto.userRole()).thenReturn(UserRole.VERIFIED);

		// 인증되지 않은 사용자
		UserDto unverifiedUserDto = Mockito.mock(UserDto.class);
		when(unverifiedUserDto.id()).thenReturn(2L);
		when(unverifiedUserDto.userRole()).thenReturn(UserRole.UNVERIFIED);

		// 일반 게시판
		BoardDto generalBoardDto = Mockito.mock(BoardDto.class);
		when(generalBoardDto.boardId()).thenReturn(1L);
		when(generalBoardDto.boardType()).thenReturn(BoardType.GENERAL);

		// 대학 게시판
		BoardDto universityBoardDto = Mockito.mock(BoardDto.class);
		when(universityBoardDto.boardId()).thenReturn(2L);
		when(universityBoardDto.boardType()).thenReturn(BoardType.UNIVERSITY);

		this.verifiedUserDto = verifiedUserDto;
		this.unverifiedUserDto = unverifiedUserDto;
		this.generalBoardDto = generalBoardDto;
		this.universityBoardDto = universityBoardDto;

		imageFile = new MockMultipartFile(
			"image",
			"test.jpg",
			"image/jpeg",
			"테스트 이미지 내용".getBytes()
		);

		images = List.of(imageFile);
		categories = List.of("카테고리1", "카테고리2");
	}

	@Test
	@DisplayName("게시글 생성 성공 - 일반 게시판, 이미지와 카테고리 모두 유효")
	void createPost_WithImagesAndCategories_Success() throws ImageException {
		// Given
		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUserDto);
		when(boardFinder.findBoardDto(anyLong())).thenReturn(generalBoardDto);
		when(postAppender.append(unverifiedUserDto.id(), boardId, title, content)).thenReturn(postId);
		when(imageValidator.filterValidImages(images)).thenReturn(images);
		when(s3Uploader.uploadFiles(images, "post")).thenReturn(List.of("image-url"));
		when(categoryResolver.resolveCategoryIds(categories, boardId)).thenReturn(List.of(1L, 2L));

		// When
		Long result = postService.createPost(boardId, title, content, images, categories);

		// Then
		assertThat(result).isEqualTo(postId);
		verify(boardValidator).validateBoardIsActive(generalBoardDto);
		verify(boardValidator).validatePostCreationAccess(unverifiedUserDto, generalBoardDto);
		verify(postAppender).append(unverifiedUserDto.id(), boardId, title, content);
		verify(imageValidator).filterValidImages(images);
		verify(s3Uploader).uploadFiles(images, "post");
		verify(postPhotoAppender).appendAll(postId, List.of("image-url"));
		verify(categoryResolver).resolveCategoryIds(categories, boardId);
		verify(postCategoryAppender).appendAll(postId, List.of(1L, 2L));
	}

	@Test
	@DisplayName("게시글 생성 성공 - 이미지와 카테고리 모두 없음")
	void createPost_WithoutImages_Success() throws ImageException {
		// Given
		List<MultipartFile> emptyImages = List.of();
		List<String> emptyCategories = List.of();

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUserDto);
		when(boardFinder.findBoardDto(anyLong())).thenReturn(generalBoardDto);

		when(postAppender.append(unverifiedUserDto.id(), boardId, title, content)).thenReturn(postId);
		when(imageValidator.filterValidImages(emptyImages)).thenReturn(emptyImages);
		when(categoryResolver.resolveCategoryIds(categories, boardId)).thenReturn(List.of());

		// When
		Long result = postService.createPost(boardId, title, content, emptyImages, emptyCategories);

		// Then
		assertThat(result).isEqualTo(postId);
		verify(boardValidator).validateBoardIsActive(generalBoardDto);
		verify(boardValidator).validatePostCreationAccess(unverifiedUserDto, generalBoardDto);
		verify(postAppender).append(unverifiedUserDto.id(), boardId, title, content);
		verify(imageValidator).filterValidImages(emptyImages);
		// S3 업로드는 호출되지 않아야 함
		verify(s3Uploader, never()).uploadFiles(any(), any());
		// 빈 이미지 리스트로 호출
		verify(postPhotoAppender).appendAll(postId, List.of());
		verify(categoryResolver).resolveCategoryIds(emptyCategories, boardId);
		// 빈 카테고리 ID 리스트로 호출
		verify(postCategoryAppender).appendAll(postId, List.of());
	}

	@Test
	@DisplayName("게시글 생성 실패 - 유효하지 않은 카테고리")
	void createPost_WithInvalidCategory_ThrowsException() throws ImageException {
		// Given
		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUserDto);
		when(boardFinder.findBoardDto(anyLong())).thenReturn(generalBoardDto);

		when(postAppender.append(unverifiedUserDto.id(), boardId, title, content)).thenReturn(postId);
		when(imageValidator.filterValidImages(images)).thenReturn(images);
		when(s3Uploader.uploadFiles(images, "post")).thenReturn(List.of("image-url"));

		// 유효하지 않은 카테고리 요청 시 예외 발생
		when(categoryResolver.resolveCategoryIds(categories, boardId))
			.thenThrow(new AppException(ErrorCode.INVALID_CATEGORY));

		// When & Then
		assertThatThrownBy(() ->
			postService.createPost(boardId, title, content, images, categories)
		).isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.INVALID_CATEGORY);

		// 게시글과 이미지는 저장됐지만 카테고리는 저장되지 않았는지 확인
		verify(postAppender).append(unverifiedUserDto.id(), boardId, title, content);
		verify(imageValidator).filterValidImages(images);
		verify(s3Uploader).uploadFiles(images, "post");
		verify(postPhotoAppender).appendAll(postId, List.of("image-url"));
		// 카테고리 검증에서 예외가 발생하므로 카테고리 추가 메서드는 호출되지 않아야 함
		verify(postCategoryAppender, never()).appendAll(anyLong(), any());
	}

	@Test
	@DisplayName("게시글 생성 실패 - 미인증 사용자, 대학 게시판")
	void createPost_UnverifiedUser_UniversityBoard_ThrowsException() {
		// Given
		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUserDto);
		when(boardFinder.findBoardDto(anyLong())).thenReturn(universityBoardDto);

		// 예외를 던지도록 설정
		Mockito.doThrow(new AppException(ErrorCode.USER_UNVERIFIED))
			.when(boardValidator).validatePostCreationAccess(unverifiedUserDto, universityBoardDto);

		// When & Then
		assertThatThrownBy(() ->
			postService.createPost(2L, title, content, images, categories)
		).isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_UNVERIFIED);

		// 예외가 발생하므로 게시글 생성 메서드는 호출되지 않아야 함
		verify(postAppender, never()).append(anyLong(), anyLong(), any(), any());
	}

	@Test
	@DisplayName("게시글 삭제 성공 - 사용자가 작성자인 경우")
	void deletePost_Success() {
		// given
		Long userId = 1L;
		when(apiUserResolver.getCurrentUserId()).thenReturn(userId);

		// 이미지 URL 목록
		List<String> imageUrls = List.of("image-url-1", "image-url-2");
		when(postPhotoFinder.findPostPhotos(postId)).thenReturn(imageUrls);

		// When
		Long result = postService.deletePost(postId);

		// Then
		assertThat(result).isEqualTo(postId);
		verify(postValidator).validatePostOwner(postId, verifiedUserDto.id());
		verify(s3Uploader).deleteFiles(imageUrls);
		verify(postPhotoDeleter).deletePostPhotos(postId);
		verify(postCategoryDeleter).deleteAllByPostId(postId);
		verify(postLikeUpdater).deleteAllByPostId(postId);
		verify(postScrapUpdater).deleteAllByPostId(postId);
		verify(commentDeleter).deleteAllByPostId(postId);
		verify(postDeleter).delete(postId);
	}

	@Test
	@DisplayName("게시글 삭제 실패 - 사용자가 작성자가 아닌 경우")
	void deletePost_NotOwner_ThrowsException() {
		// given
		Long userId = 1L;
		Long postId = 1L;
		when(apiUserResolver.getCurrentUserId()).thenReturn(userId);

		// 작성자 검증 실패 예외 발생
		Mockito.doThrow(new AppException(ErrorCode.POST_NOT_AUTHOR))
			.when(postValidator).validatePostOwner(postId, userId);

		// When & Then
		assertThatThrownBy(() ->
			postService.deletePost(postId)
		).isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.POST_NOT_AUTHOR);

		// 예외가 발생하므로 다른 메서드들은 호출되지 않아야 함
		verify(s3Uploader, never()).deleteFiles(any());
		verify(postPhotoDeleter, never()).deletePostPhotos(anyLong());
		verify(postCategoryDeleter, never()).deleteAllByPostId(anyLong());
		verify(postLikeUpdater, never()).deleteAllByPostId(anyLong());
		verify(postScrapUpdater, never()).deleteAllByPostId(anyLong());
		verify(commentDeleter, never()).deleteAllByPostId(anyLong());
		verify(postDeleter, never()).delete(anyLong());
	}

	@Test
	@DisplayName("게시글 조회 성공 - 일반 게시판, 카테고리 미지정")
	void findPosts_GeneralBoard_NoCategory_Success() {
		// Given
		Long boardId = 1L;
		int page = 1;
		PostSortType sortType = PostSortType.recent;
		String categoryName = null;

		Slice<PostWithPhotos> expectedSlice = new SliceImpl<>(List.of(), Pageable.unpaged(), false);

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUserDto); // 재학생 인증되지 않은 사용자
		when(boardFinder.findBoardDto(boardId)).thenReturn(generalBoardDto); // 일반 게시판
		when(postFinder.findPostsByCategory(boardId, page, sortType, categoryName)).thenReturn(expectedSlice);

		// When
		Slice<PostWithPhotos> result = postService.findPosts(boardId, page, sortType, categoryName);

		// Then
		assertThat(result).isEqualTo(expectedSlice);
		verify(boardValidator).validateBoardIsActive(generalBoardDto);
		verify(boardValidator).validateUniversityAccess(unverifiedUserDto, generalBoardDto);
		verify(boardValidator, never()).isCategoryEnabled(any());
		verify(postFinder).findPostsByCategory(boardId, page, sortType, categoryName);
	}

	@Test
	@DisplayName("게시글 조회 성공 - 카테고리 지정")
	void findPosts_WithCategory_Success() {
		// Given
		Long boardId = 1L;
		int page = 1;
		PostSortType sortType = PostSortType.recent;
		String categoryName = "카테고리1";

		Slice<PostWithPhotos> expectedSlice = new SliceImpl<>(List.of(), Pageable.unpaged(), false);

		when(apiUserResolver.getCurrentUserDto()).thenReturn(verifiedUserDto);
		when(boardFinder.findBoardDto(boardId)).thenReturn(generalBoardDto);
		when(postFinder.findPostsByCategory(boardId, page, sortType, categoryName)).thenReturn(expectedSlice);

		// When
		Slice<PostWithPhotos> result = postService.findPosts(boardId, page, sortType, categoryName);

		// Then
		assertThat(result).isEqualTo(expectedSlice);
		verify(boardValidator).validateBoardIsActive(generalBoardDto);
		verify(boardValidator).validateUniversityAccess(verifiedUserDto, generalBoardDto);
		verify(boardValidator).isCategoryEnabled(generalBoardDto);
		verify(postFinder).findPostsByCategory(boardId, page, sortType, categoryName);
	}

	@Test
	@DisplayName("게시글 조회 실패 - 미인증 사용자, 대학 게시판 접근")
	void findPosts_UnverifiedUser_UniversityBoard_ThrowsException() {
		// Given
		Long boardId = 2L;
		int page = 1;
		PostSortType sortType = PostSortType.recent;
		String categoryName = null;

		when(apiUserResolver.getCurrentUserDto()).thenReturn(unverifiedUserDto); // 미인증 사용자
		when(boardFinder.findBoardDto(boardId)).thenReturn(universityBoardDto); // 대학 게시판

		// 예외를 던지도록 설정
		Mockito.doThrow(new AppException(ErrorCode.USER_UNVERIFIED))
			.when(boardValidator).validateUniversityAccess(unverifiedUserDto, universityBoardDto);

		// When & Then
		assertThatThrownBy(() ->
			postService.findPosts(boardId, page, sortType, categoryName)
		).isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.USER_UNVERIFIED);

		verify(boardValidator).validateBoardIsActive(universityBoardDto);
		verify(postFinder, never()).findPostsByCategory(anyLong(), anyInt(), any(), any());
	}

	@Test
	@DisplayName("게시글 조회 실패 - 카테고리 지정, 카테고리 비활성화 게시판")
	void findPosts_WithCategory_CategoryDisabled_ThrowsException() {
		// Given
		Long boardId = 1L;
		int page = 1;
		PostSortType sortType = PostSortType.recent;
		String categoryName = "카테고리1";

		when(apiUserResolver.getCurrentUserDto()).thenReturn(verifiedUserDto);
		when(boardFinder.findBoardDto(boardId)).thenReturn(generalBoardDto);

		// 카테고리 비활성화 예외 설정
		Mockito.doThrow(new AppException(ErrorCode.CATEGORY_NOT_ALLOWED))
			.when(boardValidator).isCategoryEnabled(generalBoardDto);

		// When & Then
		assertThatThrownBy(() ->
			postService.findPosts(boardId, page, sortType, categoryName)
		).isInstanceOf(AppException.class)
			.hasFieldOrPropertyWithValue("errorCode", ErrorCode.CATEGORY_NOT_ALLOWED);

		verify(boardValidator).validateBoardIsActive(generalBoardDto);
		verify(boardValidator).validateUniversityAccess(verifiedUserDto, generalBoardDto);
		verify(postFinder, never()).findPostsByCategory(anyLong(), anyInt(), any(), any());
	}
}