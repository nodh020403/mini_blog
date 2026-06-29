package io.donghun.blog.board.service;

import io.donghun.blog.board.domain.Board;
import io.donghun.blog.board.repository.BoardRepository;
import io.donghun.blog.common.constants.ErrorCode;
import io.donghun.blog.common.exception.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BoardService {
    private final BoardRepository boardRepository;

    public Board findById(Long id) {
        return boardRepository.findById(id)
                .orElseThrow(() -> new BusinessException(ErrorCode.BOARD_NOT_FOUND));
    }
}
