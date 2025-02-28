/**@jsxImportSource @emotion/react */
import * as s from './style';
import { useUserMeQuery } from '../../queries/userQuery';
import { useEffect, useState } from 'react';
import { useUpdateNicknameMutation, useUpdateProfileImgMutation } from '../../mutations/accountMutation';
import ReactModal from 'react-modal';
import PasswordModal from '../../components/auth/PasswordModal/PasswordModal';
import { api } from '../../configs/axiosConfig';
import ChangeEmailModal from '../../components/auth/ChangeEmailModal/ChangeEmailModal';

function AccountPage(props) {
    const loginUser = useUserMeQuery();
    const updateProfileImgMutation = useUpdateProfileImgMutation();
    const updateNicknameMutation = useUpdateNicknameMutation();

    const [ emailModalOpen, setEmailModalOpen ] = useState(false);
    const [ passwordModalOpen, setPasswordModalOpen ] = useState(false);
    const [ nicknameValue, setNicknameValue ] = useState("");

    useEffect(() => {
        setNicknameValue(loginUser?.data?.data.nickname || "");
    }, [loginUser.isFetched]);

    const handleProfileImgFileOnChange = async (e) => {
        console.log({ element: e.target });
        const fileList = e.target.files;
        const file = fileList[0];

        const formData = new FormData();
        formData.append("file", file);  // back-end 에서 받을 변수명과 키 값이 동일해야함 "file"

        await updateProfileImgMutation.mutateAsync(formData);
        loginUser.refetch();
    }

    const handleNicknameInputOnChange = (e) => {
        setNicknameValue(e.target.value);
    }

    const handleSaveNicknameButtonOnClick = async () => {
        await updateNicknameMutation.mutateAsync(nicknameValue);
        loginUser.refetch();
    }

    const handleChangePasswordButtonOnClick = () => {
        setPasswordModalOpen(true);
    }
    const handleChangeEmailButtonOnClick = () => {
        setEmailModalOpen(true);
    }

    return (
        <div css={s.container}>
            <h2 css={s.title}>Account</h2>
            <div css={s.accountBox}>
                <label css={s.profileImgBox}>
                    {
                        loginUser.isLoading ||
                        <img src={`http://localhost:8080/image/user/profile/${loginUser?.data?.data.profileImg}`} alt="" />
                    }
                    <input type="file" onChange={handleProfileImgFileOnChange} />
                </label>
                <div>
                    <h3 css={s.nicknameTitle}>Preferred nickname</h3>
                    <div>
                        <input css={s.textInput} type="text" value={nicknameValue} onChange={handleNicknameInputOnChange} />
                    </div>
                    <button css={s.saveButton} onClick={handleSaveNicknameButtonOnClick} disabled={loginUser?.data?.data.nickname === nicknameValue}>Save nickname</button>
                </div>
            </div>

            <h2 css={s.title}>Account security</h2>
            <div>
                <div css={s.itemGroup}>
                    <div>
                        <h3 css={s.subTitle}>Email</h3>
                        <p css={s.subContent}>{loginUser?.data?.data.email}</p>
                    </div>
                    <button css={s.borderButton} onClick={handleChangeEmailButtonOnClick}>Change email</button>
                </div>
                {
                    !!loginUser?.data?.data.oauth2Name ||
                    <div css={s.itemGroup}>
                        <div>
                            <h3 css={s.subTitle}>Password</h3>
                            <p css={s.subContent}>계정에 로그인 할 영구 비밀번호를 설정합니다.</p>
                        </div>
                        <button css={s.borderButton} onClick={handleChangePasswordButtonOnClick}>Change password</button>
                    </div>
                }
                
            </div>
            <ReactModal
                isOpen={emailModalOpen}
                onRequestClose={() => setEmailModalOpen(false)}
                style={{
                    overlay: {
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        backgroundColor: "#00000088"
                    },
                    content: {
                        position: "static",
                        boxSizing: "border-box",
                        borderRadius: "1.5rem",
                        width: "37rem",
                    }
                }}
                children={<ChangeEmailModal setOpen={setEmailModalOpen} />}
            />
            <ReactModal
                isOpen={passwordModalOpen}
                onRequestClose={() => setPasswordModalOpen(false)}
                style={{
                    overlay: {
                        display: "flex",
                        justifyContent: "center",
                        alignItems: "center",
                        backgroundColor: "#00000088"
                    },
                    content: {
                        position: "static",
                        boxSizing: "border-box",
                        borderRadius: "1.5rem",
                        width: "37rem",
                    }
                }}
                children={<PasswordModal setOpen={setPasswordModalOpen} />}
            />
        </div>
    );
}

export default AccountPage;