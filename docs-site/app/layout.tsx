import type { Metadata } from "next";
import { DM_Mono, Fraunces, Manrope } from "next/font/google";
import "./globals.css";

const sans = Manrope({
  variable: "--font-manrope",
  subsets: ["latin"],
});

const display = Fraunces({
  variable: "--font-fraunces",
  subsets: ["latin"],
});

const mono = DM_Mono({
  variable: "--font-dm-mono",
  subsets: ["latin"],
  weight: ["400", "500"],
});

export const metadata: Metadata = {
  metadataBase: new URL("https://indunet.github.io"),
  title: {
    default: "FastProto — Binary protocols for Java",
    template: "%s — FastProto",
  },
  description:
    "Annotation-driven binary protocol toolkit for Java. Built-in checksum/CRC, formulas, and integrations.",
  openGraph: {
    title: "FastProto — Binary protocols for Java",
    description:
      "Annotation-driven binary mapping. Checksums/CRC built-in. Simple, fast, reliable.",
    type: "website",
    url: "/fastproto/",
    images: [{ url: "/fastproto/logo.png" }],
  },
};

export default function RootLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <html
      lang="en"
      className={`${sans.variable} ${display.variable} ${mono.variable} h-full scroll-smooth antialiased`}
    >
      <body className="min-h-full bg-[var(--page-bg)] font-sans text-[var(--text-main)] antialiased">
        {children}
      </body>
    </html>
  );
}
