package com.ComMangShop.Q_Board.domain.service;


import com.ComMangShop.Q_Board.domain.dto.BoardDto;
import com.ComMangShop.Q_Board.domain.dto.Criteria;
import com.ComMangShop.Q_Board.domain.dto.PageDto;
import com.ComMangShop.Q_Board.domain.dto.ReplyDto;
import com.ComMangShop.Q_Board.domain.entity.Board;
import com.ComMangShop.Q_Board.domain.entity.Reply;
import com.ComMangShop.Q_Board.domain.entity.Thumb_up;
import com.ComMangShop.Q_Board.domain.entity.User;
import com.ComMangShop.Q_Board.domain.repository.BoardRepository;
import com.ComMangShop.Q_Board.domain.repository.ReplyRepository;
import com.ComMangShop.Q_Board.domain.repository.Thumb_upRepository;
import com.ComMangShop.Q_Board.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class BoardService {


    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private ReplyRepository replyRepository;

    @Autowired
    private Thumb_upRepository thumb_upRepository;

    @Autowired
    private UserRepository userRepository;


    //모든 게시물 가져오기
    @Transactional(rollbackFor = Exception.class)
    public Map<String,Object> GetBoardList(Criteria criteria) {



        Map<String,Object> returns = new HashMap<String,Object>();


        //전체게시물 건수 받기(type,Keyword가 적용된 count로 변경
        //int totalcount = (int)boardRepository.count();

        int totalcount=0;
        if(criteria!=null&& criteria.getType()!=null) {
            if (criteria.getType().equals("title"))
                totalcount = boardRepository.countWhereTitleKeyword(criteria.getKeyword());
            else if (criteria.getType().equals("username"))
                totalcount = boardRepository.countWhereUsernameKeyword(criteria.getKeyword());
            else if (criteria.getType().equals("content"))
                totalcount = boardRepository.countWhereContentKeyword(criteria.getKeyword());
        }
        else
            totalcount = (int)boardRepository.count();


        System.out.println("COUNT  :" + totalcount);

        //PageDto 만들기
        PageDto pagedto = new PageDto(totalcount,criteria);

        //시작 게시물 번호 구하기(수정) - OFFSET
        int offset =(criteria.getPageno()-1) * criteria.getAmount();    //1page = 0, 2page = 10

        //--------------------------------------------------------
        //SEARCH
        //--------------------------------------------------------
        List<Board> list = null;
        if(criteria!=null&& criteria.getType()!=null) {
            if (criteria.getType().equals("title")) {
                list = boardRepository.findBoardTitleAmountStart(criteria.getKeyword(), pagedto.getCriteria().getAmount(), offset);
                System.out.println("TITLE SEARCH!");
                System.out.println(list);
            } else if (criteria.getType().equals("username"))
                list = boardRepository.findBoardUsernameAmountStart(criteria.getKeyword(), pagedto.getCriteria().getAmount(), offset);
            else if (criteria.getType().equals("content"))
                list = boardRepository.findBoardContentsAmountStart(criteria.getKeyword(), pagedto.getCriteria().getAmount(), offset);
            else if (criteria.getType().equals("none"))
                list = boardRepository.findBoardAmountStart(pagedto.getCriteria().getAmount(), offset);
        }
        else
            list  =  boardRepository.findBoardAmountStart(pagedto.getCriteria().getAmount(),offset);


        returns.put("list",list);
        returns.put("pageDto",pagedto);

        return returns;


    }


    @Transactional(rollbackFor = Exception.class)
    public boolean addBoard(BoardDto dto){

        //--------------------------------

        Board board = new Board();
        board.setTag(dto.getTag());
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setRegdate(LocalDateTime.now());
        board.setUsername(dto.getUsername());
        board.setCount(0L);


        board = boardRepository.save(board);

        boolean issaved = boardRepository.existsById(board.getNo());

        return issaved;
    }


    @Transactional(rollbackFor = Exception.class)

    public Board getBoardOne(Long no) {

        Optional<Board> board = boardRepository.findById(no);
        if(board.isEmpty())
            return null;
        else
            return board.get();
    }



    //----------------------------------------------------------------
    //수정
    //----------------------------------------------------------------
    @Transactional(rollbackFor = SQLException.class)
    public boolean updateBoard(BoardDto dto) throws IOException {

        //--------------------------------
        Board board = new Board();
        board.setTag(dto.getTag());
        board.setTitle(dto.getTitle());
        board.setContent(dto.getContent());
        board.setRegdate(LocalDateTime.now());
        board.setUsername(dto.getUsername());
        board.setCount(dto.getCount());



        //기존 정보 가져오기
        Board oldBoard =  boardRepository.findById(dto.getNo()).get();


        //----------------------------------------------------------------
        //코드 수정
        //----------------------------------------------------------------

        board.setNo(oldBoard.getNo());

        board =  boardRepository.save(board);


        return board!=null;

    }




    @Transactional(rollbackFor = SQLException.class)
    public boolean removeBoard(Long no) {

        Board board = boardRepository.findById(no).get();


        //DB 삭제
        boardRepository.delete(board);

        return boardRepository.existsById(no);
    }


    //----------------------------------------------------------------
    // COUNT
    //----------------------------------------------------------------
    @Transactional(rollbackFor = SQLException.class)
    public void count(Long no) {
        Board board =  boardRepository.findById(no).get();
        board.setCount(board.getCount()+1);
        boardRepository.save(board);
    }


    //----------------------------------------------------------------
    // REPLY ADD
    //----------------------------------------------------------------
    public void addReply(Long bno,String contents, String username) {
        Reply reply = new Reply();
        Board board = new Board();
        board.setNo(bno);

        reply.setBoard(board);
        reply.setContent(contents);
        reply.setUsername(username);
        reply.setRegdate(LocalDateTime.now());
        reply.setLikecount(0L);
        reply.setUnlikecount(0L);

        replyRepository.save(reply);
    }

    //----------------------------------------------------------------
    // REPLY LIST
    //----------------------------------------------------------------
    public List<ReplyDto> getReplyList(Long bno) {
        List<Reply> replyList =  replyRepository.GetReplyByBnoDesc(bno);

        List<ReplyDto> returnReply  = new ArrayList();
        ReplyDto dto = null;

        if(!replyList.isEmpty()) {
            for(int i=0;i<replyList.size();i++) {

                dto = new ReplyDto();
                dto.setBno(replyList.get(i).getBoard().getNo());
                dto.setRno(replyList.get(i).getRno());
                dto.setUsername(replyList.get(i).getUsername());
                dto.setContent(replyList.get(i).getContent());
                dto.setLikecount(replyList.get(i).getLikecount());
                dto.setUnlikecount(replyList.get(i).getUnlikecount());
                dto.setRegdate(replyList.get(i).getRegdate());

                returnReply.add(dto);

            }
            return returnReply;
        }

        return null;

    }


    public void updateReply(ReplyDto dto) {
        Reply reply =  replyRepository.findById(dto.getRno()).get();
        reply.setContent(dto.getContent());
        replyRepository.save(reply);
    }


    //----------------------------------------------------------------
    // REPLY COUNT By BNO
    //----------------------------------------------------------------

    public Long getReplyCount(Long bno) {
        return replyRepository.GetReplyCountByBnoDesc(bno);

    }

    public void deleteReply(Long rno) {
            replyRepository.deleteById(rno);
    }


    //추천 or 비추천은 1명당 1번만 가능하게 수정

    public void thumbsUp(Long rno, String username) {
        Reply reply =  replyRepository.findById(rno).get();
        User user = userRepository.findById(username).get();
        Thumb_up thumb_up = thumb_upRepository.find_rno_username(reply.getRno(),user.getUsername());

        //thumb_up  db에 현재계정과 username이 같으면서 추천을 누른 댓글의 rno와 같은 행을 찾아 추천했는지 확인
        if(thumb_up==null){
            thumb_up=new Thumb_up();
            thumb_up.setReply(reply);
            thumb_up.setUser(user);
            thumb_up.setThumb_up(true);
            thumb_upRepository.save(thumb_up);

            reply.setLikecount(reply.getLikecount() + 1L);
            replyRepository.save(reply);

            User vouchedUser=userRepository.findById(reply.getUsername()).get();

            if(vouchedUser.getVouch()==null){
                vouchedUser.setVouch(1L);
            }
            else {
                vouchedUser.setVouch(vouchedUser.getVouch() + 1L);
            }

            userRepository.save(vouchedUser);
        }
        else if(thumb_up!=null){
            if(thumb_up.getThumb_up()==true){
                reply.setLikecount(reply.getLikecount() - 1L);
                replyRepository.save(reply);
                thumb_up.setThumb_up(false);
                thumb_upRepository.save(thumb_up);

                User vouchedUser=userRepository.findById(reply.getUsername()).get();
                vouchedUser.setVouch(vouchedUser.getVouch() - 1L);
                userRepository.save(vouchedUser);

            } else if(thumb_up.getThumb_up()!=true){
                reply.setLikecount(reply.getLikecount() + 1L);
                replyRepository.save(reply);
                thumb_up.setThumb_up(true);
                thumb_upRepository.save(thumb_up);

                User vouchedUser=userRepository.findById(reply.getUsername()).get();

                if(vouchedUser.getVouch()==null){
                    vouchedUser.setVouch(1L);
                }
                else {
                    vouchedUser.setVouch(vouchedUser.getVouch() + 1L);
                }

                userRepository.save(vouchedUser);
            }
        }


    }

    public void thumbsDown(Long rno) {
        Reply reply =  replyRepository.findById(rno).get();
        reply.setUnlikecount(reply.getUnlikecount()+1L);
        replyRepository.save(reply);
    }

    //로그인한 계정의 권한 받아오기
    public String getRole(String username){
        User user = userRepository.findById(username).get();
        String role = user.getRole();
        return role;
    }

}
