import React, { useEffect } from 'react';
import { Route, Routes, useNavigate } from 'react-router-dom';
import AccountPage from '../../pages/AccountPage/AccountPage';
import MainSidebar from '../../components/common/MainSidebar/MainSidebar'
import MainContainer from '../../components/common/MainContainer/MainContainer'
import NotFoundPage from '../../pages/NotFoundPage/NotFoundPage';
import { useQueryClient } from '@tanstack/react-query';
import BoardWritePage from '../../pages/BoardWritePage/BoardWritePage';
import BoardListPage from '../../pages/BoardListPage/BoardListPage';

function MainRoute(props) {
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const queryState = queryClient.getQueryState(["userMeQuery"]);
    // const queryData = queryClient.getQueryData(["userMeQuery"]); // : 토큰이 있으면(로그인) 로그인정보 데이터 존재, 토큰이 없으면(로그아웃) undefined 즉 존재하지 않음

    useEffect(() => {
        console.log(queryState);
        if(queryState.status === "error") {
            navigate("/auth/login");
        }
    }, [queryState]);

    return queryState.status === "success" &&
    <>
        <MainSidebar />
        <MainContainer>
            <Routes>
                <Route path="/account/setting" element={<AccountPage />} />
                <Route path="/board/list" element={<BoardListPage />} />
                <Route path="/board/write/:categoryName" element={<BoardWritePage />} />
                <Route path="/*" element={<NotFoundPage />} />
            </Routes>
        </MainContainer>
    </>;
}
// 순서 : 
// queryState.status 가 처음엔 pending >> return 으로 null 반환 >> 마운트 될 준비 완료 >> useEffect 실행 >> console 찍힘(status: pending)
//  >> App.js 에서 useUserMeQuery 요청이 돌아옴 >> status: error >> queryState 가 변했기 때문에 useEffect 다시 실행 >> console 찍힘(status: error) >> /auth/login 으로 이동

export default MainRoute;
