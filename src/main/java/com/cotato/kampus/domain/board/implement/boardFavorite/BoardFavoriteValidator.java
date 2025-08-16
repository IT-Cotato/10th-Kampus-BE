package com.cotato.kampus.domain.board.implement.boardFavorite;

import com.cotato.kampus.domain.board.enums.BoardType;
import com.cotato.kampus.global.error.ErrorCode;
import com.cotato.kampus.global.error.exception.AppException;
import org.springframework.stereotype.Component;

@Component
public class BoardFavoriteValidator {

    public void validateRemovable(BoardType boardType) {
        if (boardType == BoardType.FIXED || boardType == BoardType.CARDNEWS || boardType == BoardType.UNIVERSITY) {
            throw new AppException(ErrorCode.CANNOT_REMOVE_FAVORITE_BOARD);
        }
    }
}
