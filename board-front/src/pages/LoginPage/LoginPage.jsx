/**@jsxImportSource @emotion/react */
import { Link, useNavigate, useSearchParams } from 'react-router-dom';
import * as s from './style';
import { SiGoogle, SiKakao, SiNaver } from "react-icons/si";
import { useState } from 'react';
import { useLoginMutation } from '../../mutations/authMutation';
import Swal from 'sweetalert2';
import { setTokenLocalStorage } from '../../configs/axiosConfig';
import { useQueryClient } from '@tanstack/react-query';

function LoginPage(props) {
    const navigate = useNavigate();
    const queryClient = useQueryClient();
    const loginMutation = useLoginMutation();

    const [ searchParams, setSearchParams ] = useSearchParams();
    
    const [ inputValue, setInputValue ] = useState({
        username: searchParams.get("username") || "",
        password: "",
    });

    const handleInputOnChange = (e) => {
        setInputValue(prev => ({
            ...prev,
            [e.target.name]: e.target.value,
        }));
    }
    
    const isEmpty = () => {
        const isEmptyInBox = Object.values(inputValue).map(value => !!value).includes(false);

        return isEmptyInBox;
    }

    const handleLoginOnClick = async () => {
        if(isEmpty()) {
            await Swal.fire({
                title: '로그인 실패',
                text: '사용자 정보를 입력해주세요!',
                confirmButtonText: '확인',
                confirmButtonColor: "#e22323"
            });
            return;
        }

        try {
            const response = await loginMutation.mutateAsync(inputValue);
            const tokenName = response.data.name;
            const accessToken = response.data.token;
            setTokenLocalStorage(tokenName, accessToken);
            await Swal.fire({
                icon: "success",
                text: "로그인 성공",
                timer: 1000,
                position: "center",
                showConfirmButton: false,
            });
            await queryClient.invalidateQueries({queryKey: ["userMeQuery"]}); // 캐시를 삭제하는 것이 아닌 만료를 시켜 fresh 하지 않은 상태로 바꿔줌(invalidate)
            navigate("/");
            // window.location.href("/");
            // navigate("/") 를 사용하면 위에서 이미 queryClient의 데이터를 지웠기 때문에 home 화면으로 갔다가 다시 로그인 창으로 오게 됨
            // 하지만 await 을 걸어주어 동기로 동작하게 되면 navigate 가 비동기이기 때문에 home 화면으로 갈 수 있음
        } catch(error) {
            await Swal.fire({
                title: '로그인 실패',
                text: '사용자 정보를 다시 확인해주세요!',
                confirmButtonText: '확인',
                confirmButtonColor: "#e22323"
            });
        }
    }

    return (
        <div css={s.layout}>
            <div>
                <header>
                    <h1 css={s.title1}>Think it. Make it</h1>
                    <h1 css={s.title2}>Login to your account</h1>
                </header>
                <main>
                    <div css={s.oauth2Group}>
                        <div css={s.groupBox}>
                            <button css={s.oauth2Button}>
                                <div css={s.oauth2Icon}><SiGoogle /></div>
                                <span css={s.oauth2Text}>Continue with Google</span>
                            </button>
                        </div>
                        <div css={s.groupBox}>
                            <button css={s.oauth2Button}>
                                <div css={s.oauth2Icon}><SiNaver /></div>
                                <span css={s.oauth2Text}>Continue with Naver</span>
                            </button>
                        </div>
                        <div css={s.groupBox}>
                            <button css={s.oauth2Button}>
                                <div css={s.oauth2Icon}><SiKakao /></div>
                                <span css={s.oauth2Text}>Continue with Kakao</span>
                            </button>
                        </div>
                    </div>
                    <div>
                        <div css={s.groupBox}>
                            <input css={s.textInput} type="text" placeholder='Enter your username...'
                                name="username"
                                value={inputValue.username}
                                onChange={handleInputOnChange}
                            />
                        </div>
                        <div css={s.groupBox}>
                            <input css={s.textInput} type="password" placeholder='password...'
                                name="password"
                                value={inputValue.password}
                                onChange={handleInputOnChange}
                            />
                        </div>
                        <p css={s.accountMessage}>
                            계정이 없으시다면 지금 가입하세요. <Link to={"/auth/join"}>회원가입</Link>
                        </p>
                        <div css={s.groupBox}>
                            <button css={s.accountButton} onClick={handleLoginOnClick}>Login</button>
                        </div>
                    </div>
                    <div></div>
                </main>
                <footer>
                    <p css={s.footerAgreement}>
                        이메일을 사용하여 계정을 구분하고 다른 사용자들에게 게시글을 공유합니다.
                        계속 진행하려면 약관 및 개인정보 보호정책을 이해하고 동의한다는 것을 인정해야합니다.
                    </p>
                </footer>
            </div>
        </div>
    );
}

export default LoginPage;