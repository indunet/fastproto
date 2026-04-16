import Link from "next/link";
import {
  ArrowRight,
  Binary,
  BookOpen,
  Boxes,
  Cable,
  CheckCheck,
  ChevronRight,
  Cpu,
  FileCode2,
  ShieldCheck,
  Sparkles,
} from "lucide-react";
import { SiteFooter } from "@/components/site-footer";
import { SiteHeader } from "@/components/site-header";
import { Button } from "@/components/ui/button";
import { Card } from "@/components/ui/card";
import { DOC_NAV } from "@/lib/nav";

const BADGES = [
  ["Build", "https://app.travis-ci.com/indunet/fastproto.svg?branch=master", "https://app.travis-ci.com/indunet/fastproto"],
  ["codecov", "https://codecov.io/gh/indunet/fastproto/branch/master/graph/badge.svg?token=17TEL5B5NU", "https://codecov.io/gh/indunet/fastproto"],
  ["Codacy", "https://img.shields.io/badge/Codacy-A.svg", "https://www.codacy.com/gh/indunet/fastproto/dashboard"],
  ["JetBrains", "https://img.shields.io/badge/JetBrains-support-blue", "https://www.jetbrains.com/community/opensource"],
  ["License", "https://img.shields.io/badge/license-Apache%202.0-4EB1BA.svg", "https://www.apache.org/licenses/LICENSE-2.0.html"],
] as const;

const HIGHLIGHTS = [
  ["Annotation-first", "Model packet fields directly on Java classes without drowning in masks and shifts."],
  ["Protocol-realistic", "CRC, formulas, mixed order, arrays, offsets, and edge-case packet structure are all first-class."],
  ["Ready to ship", "Use it in core JVM services, Android clients, Netty pipelines, and Kafka flows with less glue code."],
] as const;

const CAPABILITIES = [
  {
    title: "Readable mapping",
    body: "Keep the protocol layout and the class model close enough that future changes stay understandable.",
    icon: FileCode2,
  },
  {
    title: "Integrity built in",
    body: "Reach for CRC and checksum strategies without building a parallel validation layer.",
    icon: ShieldCheck,
  },
  {
    title: "Value transforms",
    body: "Encode and decode with scaling or engineering formulas where the packet format demands it.",
    icon: Sparkles,
  },
  {
    title: "Hardware detail",
    body: "Handle endianness, bit order, struct arrays, and variable length payloads without special-case chaos.",
    icon: Binary,
  },
] as const;

const USE_CASES = [
  { title: "IoT gateways", icon: Cable, body: "Device frames, telemetry payloads, and serial packet work that needs to stay maintainable." },
  { title: "Automotive & CAN", icon: Cpu, body: "Mixed-order fields and binary payloads where precision matters more than abstraction purity." },
  { title: "Industrial control", icon: Boxes, body: "Structured binary messaging for PLC, RTU, and register-driven integrations." },
] as const;

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
    <div className="site-shell flex min-h-screen flex-col">
      <script type="application/ld+json" dangerouslySetInnerHTML={{ __html: JSON.stringify(jsonLd) }} />
      <SiteHeader />

      <main className="mx-auto flex w-full max-w-7xl flex-1 flex-col gap-8 px-5 pb-6 pt-8">
        <section className="grid gap-6 lg:grid-cols-[minmax(0,1.2fr)_minmax(320px,0.8fr)]">
          <Card variant="elevated" className="paper-grid overflow-hidden px-6 py-8 md:px-8 md:py-10">
            <div className="relative z-[1] max-w-4xl">
              <span className="eyebrow">Binary Protocol Toolkit</span>
              <h1 className="font-display mt-6 max-w-3xl text-5xl leading-[0.92] tracking-[-0.06em] text-[var(--headline)] md:text-7xl">
                Java protocol mapping,
                <span className="text-gradient"> stripped of the brittle parts.</span>
              </h1>
              <p className="mt-6 max-w-2xl text-[16px] leading-8 text-[var(--text-soft)] md:text-lg">
                FastProto turns binary serialization and deserialization into a clearer modeling task. You define the
                structure once, keep the packet intent visible, and stop repeating low-level parsing code across the codebase.
              </p>

              <div className="mt-8 flex flex-wrap items-center gap-3">
                <Button variant="primary" size="lg" asChild>
                  <Link href="/help/quick-start">
                    <BookOpen />
                    Read the quick start
                  </Link>
                </Button>
                <Button variant="default" size="lg" asChild>
                  <a href="https://github.com/indunet/fastproto" target="_blank" rel="noreferrer">
                    View source on GitHub
                    <ArrowRight className="size-4" />
                  </a>
                </Button>
              </div>

              <div className="mt-8 grid gap-3 sm:grid-cols-3">
                {HIGHLIGHTS.map(([title, body]) => (
                  <div key={title} className="rounded-[22px] border border-[var(--line-soft)] bg-[rgba(255,255,255,0.5)] p-4">
                    <p className="text-sm font-semibold tracking-[-0.02em] text-[var(--headline)]">{title}</p>
                    <p className="mt-2 text-sm leading-6 text-[var(--text-soft)]">{body}</p>
                  </div>
                ))}
              </div>
            </div>
          </Card>

          <div className="grid gap-6">
            <Card className="px-6 py-6">
              <p className="text-[0.72rem] font-semibold uppercase tracking-[0.24em] text-[var(--text-dim)]">
                At a glance
              </p>
              <div className="mt-5 space-y-3">
                {[
                  ["v4.1.0", "Current documented release"],
                  ["Java 8+", "Stable baseline for adoption"],
                  ["Checksums, formulas, offsets", "Built into the same model layer"],
                ].map(([value, label]) => (
                  <div key={value} className="rounded-[20px] border border-[var(--line-soft)] bg-[rgba(255,255,255,0.48)] px-4 py-3">
                    <p className="font-display text-2xl tracking-[-0.04em] text-[var(--headline)]">{value}</p>
                    <p className="mt-1 text-sm leading-6 text-[var(--text-soft)]">{label}</p>
                  </div>
                ))}
              </div>
            </Card>

            <Card variant="accent" className="px-6 py-6">
              <div className="flex items-start gap-4">
                <div className="flex size-11 shrink-0 items-center justify-center rounded-[18px] bg-[rgba(255,255,255,0.58)] text-[var(--brand-strong)]">
                  <CheckCheck className="size-5" />
                </div>
                <div>
                  <p className="font-display text-3xl tracking-[-0.04em] text-[var(--headline)]">
                    Built for the awkward 10% most libraries ignore.
                  </p>
                  <p className="mt-3 text-sm leading-7 text-[var(--text-soft)]">
                    The last mile of binary work is usually where maintainability drops off. FastProto is strongest
                    exactly where packet definitions stop being clean and start being real.
                  </p>
                </div>
              </div>
            </Card>

            <div className="flex flex-wrap gap-2">
              {BADGES.map(([alt, src, href]) => (
                <a
                  key={alt}
                  href={href}
                  target="_blank"
                  rel="noreferrer"
                  className="rounded-full border border-[var(--line-soft)] bg-[rgba(255,255,255,0.62)] px-2.5 py-1.5 shadow-[0_10px_20px_rgba(58,41,23,0.05)]"
                >
                  {/* eslint-disable-next-line @next/next/no-img-element */}
                  <img src={src} alt={alt} className="h-5" />
                </a>
              ))}
            </div>
          </div>
        </section>

        <section className="grid gap-6 lg:grid-cols-[minmax(0,1fr)_minmax(0,0.95fr)]">
          <Card className="px-6 py-7 md:px-7">
            <span className="eyebrow">Why FastProto</span>
            <div className="mt-6 grid gap-3 sm:grid-cols-2">
              {CAPABILITIES.map(({ title, body, icon: Icon }) => (
                <div key={title} className="rounded-[24px] border border-[var(--line-soft)] bg-[rgba(255,255,255,0.48)] p-5">
                  <div className="flex size-10 items-center justify-center rounded-2xl bg-[rgba(219,232,229,0.74)] text-[var(--brand-strong)]">
                    <Icon className="size-4" />
                  </div>
                  <p className="mt-4 text-lg font-semibold tracking-[-0.02em] text-[var(--headline)]">{title}</p>
                  <p className="mt-2 text-sm leading-7 text-[var(--text-soft)]">{body}</p>
                </div>
              ))}
            </div>
          </Card>

          <Card variant="accent" className="px-6 py-7 md:px-7">
            <span className="eyebrow">Minimal Usage</span>
            <p className="font-display mt-5 max-w-xl text-4xl tracking-[-0.04em] text-[var(--headline)]">
              Small enough for the first packet, strong enough for the messy ones after it.
            </p>
            <p className="mt-4 max-w-xl text-sm leading-7 text-[var(--text-soft)]">
              You can start from a tiny model class and keep growing into checksums, transforms, and dynamic layouts
              without switching paradigms halfway through the project.
            </p>

            <div className="mt-6 overflow-hidden rounded-[26px] border border-[rgba(33,29,24,0.08)] bg-[linear-gradient(180deg,#221d18,#181512)] shadow-[0_24px_60px_rgba(21,18,15,0.22)]">
              <div className="flex items-center gap-2 border-b border-white/8 px-4 py-3">
                <span className="size-2.5 rounded-full bg-[#f39a82]" />
                <span className="size-2.5 rounded-full bg-[#f2c36f]" />
                <span className="size-2.5 rounded-full bg-[#82c69b]" />
                <span className="ml-3 text-[11px] font-semibold uppercase tracking-[0.18em] text-white/45">
                  Packet.java
                </span>
              </div>
              <pre className="overflow-x-auto px-5 py-5 font-mono text-sm leading-7 text-[#d8eadf]">
                <code>{`import org.indunet.fastproto.FastProto;
import org.indunet.fastproto.annotation.UInt8Type;

public class Packet {
  @UInt8Type(offset = 0)
  int id;
}

byte[] raw = new byte[] { 1 };
Packet packet = FastProto.decode(raw, Packet.class);
byte[] out = FastProto.encode(packet, 1);`}</code>
              </pre>
            </div>
          </Card>
        </section>

        <section className="grid gap-6 lg:grid-cols-[minmax(0,0.95fr)_minmax(0,1.05fr)]">
          <Card className="px-6 py-7 md:px-7">
            <div className="flex items-end justify-between gap-4">
              <div>
                <span className="eyebrow">Documentation</span>
                <p className="font-display mt-5 text-4xl tracking-[-0.04em] text-[var(--headline)]">
                  Start from the path that matches your problem.
                </p>
              </div>
              <Button variant="outline" size="sm" asChild>
                <Link href="/help/quick-start">
                  Browse docs
                  <ChevronRight className="size-4" />
                </Link>
              </Button>
            </div>

            <div className="mt-7 grid gap-3">
              {DOC_NAV.slice(0, 5).map((item) => (
                <Link
                  key={item.slug}
                  href={`/help/${item.slug}`}
                  className="group rounded-[22px] border border-[var(--line-soft)] bg-[rgba(255,255,255,0.46)] px-4 py-4"
                >
                  <div className="flex items-start justify-between gap-4">
                    <div>
                      <p className="text-[11px] font-semibold uppercase tracking-[0.18em] text-[var(--text-dim)]">
                        {item.kicker}
                      </p>
                      <p className="mt-1 text-base font-semibold tracking-[-0.02em] text-[var(--headline)]">{item.title}</p>
                      <p className="mt-1.5 text-sm leading-6 text-[var(--text-soft)]">{item.summary}</p>
                    </div>
                    <ArrowRight className="mt-1 size-4 shrink-0 text-[var(--text-dim)] transition-transform group-hover:translate-x-1" />
                  </div>
                </Link>
              ))}
            </div>
          </Card>

          <div className="grid gap-6">
            <Card className="px-6 py-7 md:px-7">
              <span className="eyebrow">Use Cases</span>
              <div className="mt-6 grid gap-3">
                {USE_CASES.map(({ title, icon: Icon, body }) => (
                  <div key={title} className="rounded-[22px] border border-[var(--line-soft)] bg-[rgba(255,255,255,0.46)] p-4">
                    <div className="flex items-start gap-3">
                      <div className="mt-0.5 flex size-10 shrink-0 items-center justify-center rounded-2xl bg-[rgba(240,224,206,0.68)] text-[var(--accent)]">
                        <Icon className="size-4" />
                      </div>
                      <div>
                        <p className="text-base font-semibold tracking-[-0.02em] text-[var(--headline)]">{title}</p>
                        <p className="mt-1.5 text-sm leading-6 text-[var(--text-soft)]">{body}</p>
                      </div>
                    </div>
                  </div>
                ))}
              </div>
            </Card>

            <Card className="px-6 py-7 md:px-7">
              <span className="eyebrow">Install</span>
              <p className="font-display mt-5 text-4xl tracking-[-0.04em] text-[var(--headline)]">
                Add the dependency and move straight into the mapping layer.
              </p>

              <div className="mt-6 space-y-4">
                <div className="rounded-[22px] border border-[var(--line-soft)] bg-[rgba(255,255,255,0.5)] p-4">
                  <p className="text-[11px] font-semibold uppercase tracking-[0.18em] text-[var(--text-dim)]">Maven</p>
                  <pre className="mt-3 overflow-x-auto rounded-[18px] bg-[rgba(34,29,24,0.96)] px-4 py-4 font-mono text-xs leading-7 text-[#d7ebdf]">
                    <code>{`<dependency>
  <groupId>org.indunet</groupId>
  <artifactId>fastproto</artifactId>
  <version>4.1.0</version>
</dependency>`}</code>
                  </pre>
                </div>

                <div className="rounded-[22px] border border-[var(--line-soft)] bg-[rgba(255,255,255,0.5)] p-4">
                  <p className="text-[11px] font-semibold uppercase tracking-[0.18em] text-[var(--text-dim)]">Gradle</p>
                  <pre className="mt-3 overflow-x-auto rounded-[18px] bg-[rgba(34,29,24,0.96)] px-4 py-4 font-mono text-xs leading-7 text-[#d7ebdf]">
                    <code>{`implementation("org.indunet:fastproto:4.1.0")`}</code>
                  </pre>
                </div>
              </div>
            </Card>
          </div>
        </section>
      </main>

      <SiteFooter />
    </div>
  );
}
