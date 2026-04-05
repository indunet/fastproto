import type { Metadata } from "next";
import { Geist, Geist_Mono } from "next/font/google";
import "./globals.css";

const geistSans = Geist({
  variable: "--font-geist-sans",
  subsets: ["latin"],
});

const geistMono = Geist_Mono({
  variable: "--font-geist-mono",
  subsets: ["latin"],
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
      className={`${geistSans.variable} ${geistMono.variable} h-full scroll-smooth antialiased`}
    >
      <body className="min-h-full bg-white font-sans text-gray-700 antialiased">
        {children}
      </body>
    </html>
  );
}
