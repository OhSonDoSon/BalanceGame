import Link from "next/link";
import React from "react";
import * as S from "styles/components.style";

const Header = () => (
  <S.HeaderWrapper>
    <nav>
      <Link href="/">Main</Link>
      <Link href="/">sth</Link>
    </nav>
  </S.HeaderWrapper>
);

export default Header;
