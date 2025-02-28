/**@jsxImportSource @emotion/react */
import * as s from './style';
import { RiCloseCircleFill } from "react-icons/ri";
import { CgMail } from "react-icons/cg";
import { useEffect, useState } from 'react';

function ChangeEmailModal({ setOpen }) {

    const [emailValue, setEmailValue] = useState("");
    const [ time, setTime ] = useState(1000 * 60 * 5); 
    const [ isSend, setSend ] = useState(false);
    
    useEffect(() => {
        const timer = setInterval(() => {
            setTime(prev => prev - 1000);
        }, 1000);
        return () => {
            clearInterval(timer);
        }
    },[isSend]);

    const handleEmailInputOnChange = (e) => {
        setEmailValue(e.target.value);
    }

    const handleSendMailButtonOnClick = () => {
        setTime(1000 * 60 * 5);
        setSend(true);
    }

    const handleSetButtonOnClick = () => {
        
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
                            value={emailValue}
                            onChange={handleEmailInputOnChange} />
                        {
                            isSend
                            ?
                            <span>{time}</span> 
                            :
                            <button onClick={handleSendMailButtonOnClick} disabled={isSend}>전송</button>
                        }
                    </div>
                </div>
                <button
                    css={s.setButton}
                    disabled={!emailValue}
                    onClick={handleSetButtonOnClick}
                >Set a email address</button>
            </div>
        </div>
    );
}

export default ChangeEmailModal;