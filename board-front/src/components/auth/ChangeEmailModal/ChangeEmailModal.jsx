/**@jsxImportSource @emotion/react */
import * as s from './style';
import { RiCloseCircleFill } from "react-icons/ri";
import { CgMail } from "react-icons/cg";
import { useEffect, useState } from 'react';
import Swal from 'sweetalert2';
import { useSendVerifyEmailMutation, useUpdateEmailMutation } from '../../../mutations/accountMutation';
import { useQueryClient } from '@tanstack/react-query';

function ChangeEmailModal({ setOpen }) {
    const queryClient = useQueryClient();
    const verifyEmailMutation = useSendVerifyEmailMutation();
    const updateEmailMutation = useUpdateEmailMutation();

    const [emailValue, setEmailValue] = useState("");
    const [ time, setTime ] = useState(60 * 5); 
    const [ isSend, setSend ] = useState(false);
    const [ verifyInputValue, setVerifyInputValue ] = useState({
        first: "",
        second: "",
        third: "",
        fourth: "",
        fifth: "",
        sixth: "",
    });
    const [ verifyCode, setVerifyCode ] = useState("");
    
    useEffect(() => {
        const timer = setInterval(() => {
            setTime(prev => prev > 0 ? prev - 1 : 0);
        }, 1000);
        return () => {
            clearInterval(timer);
        }
    }, [isSend]);
    
    useEffect(() => {
        if (time === 0) {
            Swal.fire({
                showConfirmButton: true,
                confirmButtonText: "확인",
                titleText: "인증 시간이 만료되었습니다.",
            }).then(() => {
                setOpen(false)
            })
        }
    }, [time])

    const handleEmailInputOnChange = (e) => {
        setEmailValue(e.target.value);
    }

    const handleSendMailButtonOnClick = async () => {
        setTime(60 * 5);
        setSend(true);
        const response = await verifyEmailMutation.mutateAsync(emailValue);
        setVerifyCode(response.data.toString().padStart(6, '0'));
    }

    const handleVerifyInputOnChange = (e) => {
        setVerifyInputValue(prev => {
            if(/^[0-9]?$/.test(e.target.value)) {
                return {
                    ...prev,
                    [e.target.name]: e.target.value,
                }
            }
            return {
                ...prev,
            }
        });
    }

    const handleSetButtonOnClick = async () => {
        const inputCode =
            verifyInputValue.first
            + verifyInputValue.second
            + verifyInputValue.third
            + verifyInputValue.fourth
            + verifyInputValue.fifth
            + verifyInputValue.sixth;
        
        if(verifyCode.toString() !== inputCode) {
            await Swal.fire({
                titleText: "인증번호가 일치하지 않습니다.",
                confirmButtonText: "확인",
                confirmButtonColor: "#d02121",
            });
            return;
        }
        await updateEmailMutation.mutateAsync(emailValue);
        await Swal.fire({
            titleText: "이메일 변경 완료.",
            confirmButtonText: "확인",
        });
        await queryClient.invalidateQueries({queryKey: ["userMeQuery"]});
        setOpen(false);
    }

    const handleCloseButtonOnClick = () => {
        setOpen(false);
    }

    return (
        <div>
            <div css={s.modalTop}>
                <div onClick={handleCloseButtonOnClick}><RiCloseCircleFill /></div>
            </div>
            <div css={s.header}>
                <div css={s.headerIcon}><CgMail /></div>
                <h2 css={s.headerTitle}>Set a email address</h2>
                <p css={s.headerMessage}>변경할 이메일 주소를 입력하세요. 이후 인증 절차를 통해 이메일 변경이 가능합니다.</p>
            </div>
            <div>
                <div css={s.inputGroup}>
                    <label>Enter a new email</label>
                    <div css={s.emailInputAndSendButton}>
                        <input type="email" name='newEmail'
                            disabled={isSend}
                            value={emailValue}
                            onChange={handleEmailInputOnChange} />
                        {
                            isSend
                            ? // padStart(2, '0') >> 2자리 글자, 빈자리에는 0 채우기 >> 5분 -> 05분
                                <span>{Math.floor(time / 60).toString().padStart(2, '0')}:{(time % 60).toString().padStart(2, '0')}</span> 
                            :
                            <button onClick={handleSendMailButtonOnClick} disabled={!emailValue}>전송</button>
                        }
                    </div>
                </div>
                {
                    isSend &&
                    <div css={s.inputGroup}>
                        <div css={s.verifyInput}>
                            <input type="number" name='first' value={verifyInputValue.first} onChange={handleVerifyInputOnChange} />
                            <input type="number" name='second' value={verifyInputValue.second} onChange={handleVerifyInputOnChange} />
                            <input type="number" name='third' value={verifyInputValue.third} onChange={handleVerifyInputOnChange} />
                            <input type="number" name='fourth' value={verifyInputValue.fourth} onChange={handleVerifyInputOnChange} />
                            <input type="number" name='fifth' value={verifyInputValue.fifth} onChange={handleVerifyInputOnChange} />
                            <input type="number" name='sixth' value={verifyInputValue.sixth} onChange={handleVerifyInputOnChange} />
                        </div>
                    </div>
                }
                <button
                    css={s.setButton}
                    disabled={!emailValue || Object.values(verifyInputValue).includes("")}
                    onClick={handleSetButtonOnClick}
                >Set a email address</button>
            </div>
        </div>
    );
}

export default ChangeEmailModal;