"use client";

import Link from "next/link";
import Header from "components/Header";
// import Layout from "../components/Layout";
import * as S from "styles/page.style";
import { ThemeProvider } from "styled-components";
import { colors } from "styles/theme";
import { AppProps } from "next/app";

const IndexPage = ({ pageProps }: AppProps) => (
  <ThemeProvider theme={colors}>
    <S.IndexPageWrapper {...pageProps}>
      <S.MainPageBody>
        <Header></Header>
        {/* <Layout title="Home | Next.js + TypeScript Example"> */}
        <h1>Hello Next.js 👋</h1>
        <p>
          <Link href="/about">About</Link>
        </p>
        {/* </Layout> */}
      </S.MainPageBody>
    </S.IndexPageWrapper>
  </ThemeProvider>
);

export default IndexPage;
