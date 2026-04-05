import Link from "next/link";
import { SiteFooter } from "@/components/site-footer";
import { SiteHeader } from "@/components/site-header";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import {
  BadgeCheck,
  Binary,
  BookOpen,
  CheckCircle2,
  Layers,
  ListOrdered,
  ShieldCheck,
  Sparkles,
  ArrowRight,
} from "lucide-react";

const BADGES = [
  ["Build",     "https://app.travis-ci.com/indunet/fastproto.svg?branch=master",                                  "https://app.travis-ci.com/indunet/fastproto"],
  ["codecov",   "https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU",        "https://codecov.io/gh/indunet/fastproto"],
  ["Codacy",    "https://img.shields.io/badge/Codacy-A.svg",                                                     "https://www.codacy.com/gh/indunet/fastproto/dashboard"],
  ["JetBrains", "https://img.shields.io/badge/JetBrains-support-blue",                                           "https://www.jetbrains.com/community/opensource"],
  ["License",   "https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg",                                  "https://www.apache.org/licenses/LICENSE-2.0.html"],
] as const;

const FEATURES = [
  { title: "Annotation-driven",   body: "Map binary data to Java fields via simple annotations.",                  icon: BookOpen     },
  { title: "Broad type support",  body: "Primitives, unsigned, strings, time, arrays and collections.",            icon: Layers       },
  { title: "Flexible addressing", body: "Reverse addressing for variable-length packets.",                          icon: Binary       },
  { title: "Byte & bit order",    body: "Big-endian or little-endian; MSB/LSB bit ordering.",                      icon: ListOrdered  },
  { title: "Custom formulas",     body: "Apply lambdas or classes to transform values on encode/decode.",           icon: Sparkles     },
  { title: "Checksum / CRC",      body: "Single annotation: CRC8/16/32/64, LRC, XOR, sum.",                       icon: ShieldCheck  },
  { title: "Multiple APIs",       body: "Fluent builder and static helpers for all use cases.",                     icon: CheckCircle2 },
];

const jsonLd = {
  "@context": "https://schema.org",
  "@type": "SoftwareApplication",
  name: "FastProto",
  applicationCategory: "DeveloperApplication",
  operatingSystem: "Cross-platform",
  description: "Annotation-driven binary protocol toolkit for Java. Built-in checksum/CRC support.",
  softwareVersion: "4.1.0",
  license: "https://www.apache.org/licenses/LICENSE-2.0",
  url: "https://indunet.github.io/fastproto/",
  downloadUrl: "https://repo1.maven.org/maven2/org/indunet/fastproto/",
  programmingLanguage: "Java",
};

export default function HomePage() {
  return (
    <div className="flex min-h-screen flex-col bg-white">
      <script type="application/ld+json" dangerouslySetInnerHTML={{ __html: JSON.stringify(jsonLd) }} />
      <SiteHeader />

      {/* ═══ Hero ══════════════════════════════════════════════ */}
      <section className="border-b border-gray-100 bg-gradient-to-b from-blue-50/60 to-white px-5 py-16 text-center">
        {/* Version chip — no glow, clean and readable */}
        <div className="mx-auto mb-6 inline-flex items-center gap-1.5 rounded-full border border-blue-200 bg-white px-3 py-1 text-xs font-semibold text-blue-700 shadow-sm">
          <span className="text-blue-400">v</span>4.1.0
          <span className="mx-1 h-3 w-px bg-blue-200" />
          Apache 2.0
        </div>

        <h1 className="text-gradient mx-auto max-w-2xl text-4xl font-extrabold tracking-tight sm:text-5xl">
          FastProto
        </h1>
        <p className="mx-auto mt-4 max-w-xl text-lg text-gray-500">
          Annotation-driven binary protocol serialization for Java.
        </p>
        <p className="mx-auto mt-1 max-w-xl text-sm text-gray-400">
          Designed for IoT, automotive, industrial control &amp; energy metering.
        </p>

        <div className="mt-8 flex flex-wrap items-center justify-center gap-3">
          <Button variant="primary" size="lg" asChild>
            <Link href="/help/quick-start">
              <BookOpen />
              Get Started
            </Link>
          </Button>
          <Button variant="default" size="lg" asChild>
            <a href="https://github.com/indunet/fastproto" target="_blank" rel="noreferrer">
              View on GitHub
              <ArrowRight className="size-4 opacity-60" />
            </a>
          </Button>
        </div>

        {/* CI/CD badges */}
        <div className="mt-8 flex flex-wrap items-center justify-center gap-2">
          {BADGES.map(([alt, src, href]) => (
            <a key={alt} href={href} target="_blank" rel="noreferrer"
               className="opacity-75 transition-opacity hover:opacity-100">
              {/* eslint-disable-next-line @next/next/no-img-element */}
              <img src={src} alt={alt} className="h-5" />
            </a>
          ))}
        </div>
      </section>

      <div className="mx-auto w-full max-w-5xl flex-1 space-y-8 px-5 py-10">

        {/* ═══ What is FastProto ══════════════════════════════ */}
        <section>
          <h2 className="mb-1 text-xs font-semibold uppercase tracking-widest text-blue-600">
            Overview
          </h2>
          <p className="mt-2 text-2xl font-bold text-gray-900">What is FastProto?</p>
          <p className="mt-3 max-w-3xl leading-relaxed text-gray-600">
            FastProto is a high-performance Java library for binary protocol serialization and deserialization.
            Define complex byte-stream structures with annotations — no manual bit-shifting, no boilerplate.
            Built for production use cases including IoT gateways, CAN bus parsers, UART framing and
            Modbus register mapping.
          </p>
        </section>

        {/* ═══ Philosophy + Features ══════════════════════════ */}
        <div className="grid gap-6 lg:grid-cols-2">

          {/* Design philosophy */}
          <Card className="p-6">
            <div className="mb-4 flex items-center gap-2">
              <BadgeCheck className="size-4 shrink-0 text-blue-600" />
              <span className="text-sm font-semibold text-gray-900">Design philosophy</span>
            </div>
            <ul className="space-y-4 text-sm">
              {[
                ["Declarative over imperative",     "Protocol structure expressed through annotations — the code reads like the protocol spec."],
                ["Performance with maintainability", "Reflection metadata cached at startup; encode/decode paths are allocation-light."],
                ["Grounded in engineering reality",  "Mixed endianness, sub-byte fields, BCD encoding, CRC variants — all handled natively."],
              ].map(([title, desc]) => (
                <li key={title} className="flex gap-2">
                  <span className="mt-0.5 size-1.5 shrink-0 translate-y-[5px] rounded-full bg-blue-500" />
                  <span>
                    <span className="font-semibold text-gray-800">{title}: </span>
                    <span className="text-gray-500">{desc}</span>
                  </span>
                </li>
              ))}
            </ul>
          </Card>

          {/* Key features */}
          <Card className="p-6">
            <p className="mb-4 text-sm font-semibold text-gray-900">Key features</p>
            <div className="grid grid-cols-2 gap-2">
              {FEATURES.map(({ title, body, icon: Icon }, i) => (
                <div
                  key={title}
                  className={[
                    "rounded-lg border border-gray-100 bg-gray-50/70 p-3",
                    "transition-colors hover:border-blue-200 hover:bg-blue-50/50",
                    /* Alternating diagonal corner radii */
                    i % 2 === 0
                      ? "rounded-tl-xl rounded-br-xl rounded-tr-md rounded-bl-md"
                      : "rounded-tr-xl rounded-bl-xl rounded-tl-md rounded-br-md",
                  ].join(" ")}
                >
                  <div className="flex items-center gap-1.5 text-xs font-semibold text-gray-700">
                    <Icon className="size-3.5 shrink-0 text-blue-500" />
                    {title}
                  </div>
                  <p className="mt-1 text-xs leading-relaxed text-gray-500">{body}</p>
                </div>
              ))}
            </div>
          </Card>
        </div>

        {/* ═══ Basic usage ════════════════════════════════════ */}
        <Card className="p-6">
          <h2 className="mb-1 text-xs font-semibold uppercase tracking-widest text-blue-600">
            Basic usage
          </h2>
          <p className="mb-4 mt-1 text-sm text-gray-500">
            Annotate fields, call <code className="rounded bg-gray-100 px-1 py-0.5 text-xs font-mono text-blue-700">FastProto.decode</code> — done.
          </p>
          <pre className="overflow-x-auto rounded-lg border border-slate-700/40 bg-slate-900 p-5 font-mono text-sm leading-relaxed text-sky-300">
            <code>{`import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.UInt8Type;

public class Packet {
  @UInt8Type(offset = 0) int id;
}

byte[] bytes = new byte[]{1};
Packet p = FastProto.decode(bytes, Packet.class);
byte[] out = FastProto.encode(p, 1);`}</code>
          </pre>
        </Card>

        {/* ═══ Get started + Install ══════════════════════════ */}
        <div className="grid gap-6 lg:grid-cols-2">

          <Card className="p-6">
            <h2 className="mb-1 text-xs font-semibold uppercase tracking-widest text-blue-600">
              Documentation
            </h2>
            <p className="mt-2 mb-4 text-sm text-gray-500">Guides to get you up and running:</p>
            <ul className="space-y-2 text-sm">
              {[
                ["quick-start",         "Quick Start"],
                ["annotation-mapping",  "Annotation Mapping"],
                ["variable-length",     "Variable Length"],
                ["dynamic-offset",      "Dynamic Offset (offsetRef)"],
                ["checksum",            "Checksum / CRC"],
                ["formulas",            "Transformation Formulas"],
                ["without-annotations", "APIs without Annotations"],
                ["android",             "Android"],
              ].map(([slug, label]) => (
                <li key={slug}>
                  <Link
                    href={`/help/${slug}`}
                    className="flex items-center gap-1.5 text-blue-600 hover:text-blue-800 hover:underline"
                  >
                    <ArrowRight className="size-3 shrink-0 opacity-50" />
                    {label}
                  </Link>
                </li>
              ))}
            </ul>
          </Card>

          <Card className="p-6">
            <h2 className="mb-1 text-xs font-semibold uppercase tracking-widest text-blue-600">
              Installation
            </h2>
            <p className="mt-2 mb-3 text-sm text-gray-500">
              Available on Maven Central. Requires Java 8+.
            </p>
            <p className="text-xs font-semibold text-gray-500">Maven</p>
            <pre className="mt-1.5 overflow-x-auto rounded-lg border border-slate-700/40 bg-slate-900 p-4 font-mono text-xs leading-relaxed text-sky-300">
              <code>{`<dependency>
  <groupId>org.indunet</groupId>
  <artifactId>fastproto</artifactId>
  <version>4.1.0</version>
</dependency>`}</code>
            </pre>
            <p className="mt-3 text-xs font-semibold text-gray-500">Gradle</p>
            <pre className="mt-1.5 overflow-x-auto rounded-lg border border-slate-700/40 bg-slate-900 p-4 font-mono text-xs text-sky-300">
              <code>{`implementation "org.indunet:fastproto:4.1.0"`}</code>
            </pre>
            <div className="mt-5 flex gap-2">
              <Button variant="primary" asChild>
                <Link href="/help/quick-start"><BookOpen />Read the docs</Link>
              </Button>
            </div>
          </Card>
        </div>

        {/* ═══ License ════════════════════════════════════════ */}
        <div className="rounded-xl border border-gray-100 bg-gray-50 px-6 py-5">
          <p className="text-sm text-gray-500">
            <span className="font-semibold text-gray-700">Open source.</span>{" "}
            Released under the{" "}
            <a className="font-medium text-blue-600 hover:underline"
               href="https://www.apache.org/licenses/LICENSE-2.0.html"
               target="_blank" rel="noreferrer">
              Apache License 2.0
            </a>
            . Source on{" "}
            <a className="font-medium text-blue-600 hover:underline"
               href="https://github.com/indunet/fastproto"
               target="_blank" rel="noreferrer">
              GitHub
            </a>
            . Artifacts on{" "}
            <a className="font-medium text-blue-600 hover:underline"
               href="https://search.maven.org/artifact/org.indunet/fastproto/4.1.0/jar"
               target="_blank" rel="noreferrer">
              Maven Central
            </a>
            .
          </p>
        </div>

      </div>
      <SiteFooter />
    </div>
  );
}
