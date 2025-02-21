import { css } from "@emotion/react";

export const basicButton = css`
    display: flex;
    justify-content: center;
    align-items: center;
    
    box-sizing: border-box;
    border: none;
    border-radius: 0.7rem;
    padding: 0.6rem;

    background-color: transparent;

    font-size: 2.5rem;
    cursor: pointer;

    &:hover {
        background-color: #00000011;
    }

    &:active {
        background-color: #00000022;
    }
`;