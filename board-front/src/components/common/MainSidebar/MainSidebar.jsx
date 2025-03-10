/**@jsxImportSource @emotion/react */
import * as s from './style';
import { basicButton, emptyButton } from '../../../styles/buttons';
import { FiChevronsLeft } from "react-icons/fi";
import { useRecoilState } from 'recoil';
import { mainSidebarIsOpenState } from '../../../atoms/mainSidebar/mainSidebarAtom';
import { LuLockKeyhole } from "react-icons/lu";
import { BiEdit, BiLogOut } from "react-icons/bi";
import { Link, useNavigate } from 'react-router-dom';
import { setTokenLocalStorage } from '../../../configs/axiosConfig';
import { useQueryClient } from '@tanstack/react-query';
import Swal from 'sweetalert2';
import { useGetCategories } from '../../../queries/boardQuery';


function MainSidebar(props) {
    const navigate = useNavigate();
    const [ isOpen, setOpen ] = useRecoilState(mainSidebarIsOpenState);
    const queryClient = useQueryClient();
    const loginUserData = queryClient.getQueryData(["userMeQuery"]);
    const categories = useGetCategories();

    const handleSidebarClose = () => {
        setOpen(false);
    }

    const handleAccountButtonOnClick = () => {
        navigate("/account/setting");
    }

    const handleLogoutButtonOnClick = async () => {
        setTokenLocalStorage("AccessToken", null);
        await queryClient.invalidateQueries({queryKey: ["userMeQuery"]});
        navigate("/auth/login");
    }

    const handleWriteButtonOnClick = async (categoryName) => {
        if (!categoryName) {
            const categoryData = await Swal.fire({
                title: "카테고리명을 입력하세요",
                input: "text",
                inputPlaceholder: "Enter category name...",
                showCancelButton: true,
                confirmButtonText: "작성하기",
                cancelButtonText: "취소하기",
            });
            if (categoryData.isConfirmed) {
                categoryName = categoryData.value;
            } else {
                return;
            }
        }
        navigate(`/board/write/${categoryName}`);
    }

    return (
        <div css={s.layout(isOpen)}>
            <div css={s.container}>
                <div>
                    <div css={s.groupLayout}>
                        <div css={s.topGroup}>
                            <div css={s.user}>
                                <button css={emptyButton} onClick={handleAccountButtonOnClick}>
                                    <span css={s.authText}>
                                        <div css={s.profileImgBox}>
                                            <img src={`http://localhost:8080/image/user/profile/${loginUserData?.data.profileImg}`} alt="" />
                                        </div>
                                        <span>{loginUserData?.data.nickname}</span>
                                    </span>
                                </button>
                            </div>
                            <button css={basicButton} onClick={handleSidebarClose}><FiChevronsLeft /></button>
                        </div>
                    </div>
                    <div css={s.groupLayout}>
                        <Link to={"/board/list?page=1&order=recent&searchText="}>
                            <button css={emptyButton}>
                                <span>
                                    전체 게시글
                                </span>
                            </button>
                        </Link>
                    </div>
                    <div css={s.groupLayout}>
                        <button css={emptyButton}>
                            <span>
                                공지사항
                            </span>
                        </button>
                    </div>
                    <div css={s.groupLayout}>
                        <div css={s.categoryItem}>
                            <button css={emptyButton}>내가 작성한 글({categories.isLoading || categories.data.data.reduce((prev, category) => prev + category.boardCount, 0)})</button> 
                            {/* reduce() >> 반복 돌면서 어떠한 연산을 해줌. 괄호 안에는 어떠한 연산을 할 지에 대한 함수가 들어간다. */}
                            {/* (prev, category) => prev + category.boardCount : callback 함수, 0 은 prev의 초기값 */}
                            {/* 첫번째 연산: 0 + 첫번째 카테고리의 boardCount > 리턴값이 다음 연산의 prev에 들어감 >> callback */}
                            <button css={basicButton} onClick={() => handleWriteButtonOnClick(null)}><BiEdit /></button>
                        </div>
                    </div>
                </div>
                <div css={s.categoryListContainer}>
                    {
                        categories.isLoading ||
                        categories.data.data.map(category =>  // map 을 돌릴 땐, key 값 지정 필수(key 값은 유일한 값이어야 함)
                            <div key={category.boardCategoryId} css = {s.groupLayout}>
                                <div css={s.categoryItem}>
                                    <button css={emptyButton}>{category.boardCategoryName}({category.boardCount})</button>
                                    <button css={basicButton} onClick={() => handleWriteButtonOnClick(category.boardCategoryName)}><BiEdit /></button>
                                </div>
                            </div>
                        )
                    }
                </div>
                <div>
                    <div css={s.groupLayout}>
                        <button css={emptyButton} onClick={handleLogoutButtonOnClick}>
                            <span css={s.authText}>
                                <BiLogOut /> 로그아웃
                            </span>
                        </button>
                    </div>
                </div>
            </div>
        </div>
    );
}

export default MainSidebar;