import * as React from "react";
import { Slot } from "@radix-ui/react-slot";
import { cva, type VariantProps } from "class-variance-authority";
import { cn } from "@/lib/cn";

const buttonVariants = cva(
  [
    "inline-flex items-center justify-center gap-2 whitespace-nowrap rounded-full",
    "text-sm font-semibold tracking-[-0.01em]",
    "transition-all duration-200",
    "focus-visible:outline-none focus-visible:ring-2 focus-visible:ring-[rgba(110,141,137,0.3)] focus-visible:ring-offset-2",
    "focus-visible:ring-offset-[var(--page-bg)]",
    "disabled:pointer-events-none disabled:opacity-40",
    "[&_svg]:pointer-events-none [&_svg]:size-4 [&_svg]:shrink-0",
  ].join(" "),
  {
    variants: {
      variant: {
        primary: [
          "border border-[rgba(78,108,104,0.24)] text-white",
          "bg-[linear-gradient(135deg,#5c7b77,#6e8d89_55%,#89a5a1)]",
          "shadow-[0_14px_30px_rgba(78,108,104,0.22)]",
          "hover:-translate-y-0.5 hover:shadow-[0_20px_36px_rgba(78,108,104,0.24)]",
          "active:translate-y-0",
        ].join(" "),
        default: [
          "border border-[rgba(89,73,53,0.12)] text-[var(--headline)]",
          "bg-[rgba(255,252,248,0.88)] shadow-[0_10px_28px_rgba(58,41,23,0.08)]",
          "hover:-translate-y-0.5 hover:border-[rgba(89,73,53,0.22)] hover:bg-white",
          "active:translate-y-0",
        ].join(" "),
        outline: [
          "border border-[rgba(110,141,137,0.28)] bg-transparent text-[var(--brand-strong)]",
          "hover:bg-[rgba(219,232,229,0.32)] hover:border-[rgba(110,141,137,0.42)]",
          "active:bg-[rgba(219,232,229,0.44)]",
        ].join(" "),
        ghost: [
          "border border-transparent bg-transparent text-[var(--text-soft)]",
          "hover:bg-[rgba(255,255,255,0.56)] hover:text-[var(--headline)]",
        ].join(" "),
      },
      size: {
        default: "h-11 px-5 py-2.5",
        sm: "h-9 px-3.5 text-xs",
        lg: "h-12 px-6 text-[0.95rem]",
        icon: "h-11 w-11 px-0",
      },
    },
    defaultVariants: {
      variant: "default",
      size: "default",
    },
  },
);

export interface ButtonProps
  extends React.ButtonHTMLAttributes<HTMLButtonElement>,
    VariantProps<typeof buttonVariants> {
  asChild?: boolean;
}

const Button = React.forwardRef<HTMLButtonElement, ButtonProps>(
  ({ className, variant, size, asChild = false, ...props }, ref) => {
    const Comp = asChild ? Slot : "button";
    return (
      <Comp
        className={cn(buttonVariants({ variant, size, className }))}
        ref={ref}
        {...props}
      />
    );
  },
);
Button.displayName = "Button";

export { Button, buttonVariants };
