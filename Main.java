public class Main {
	private void alias() {
		int a = b;
		int c = 1;
		int d = e;
		a = 5;
		d = c;
	}

	private void alias_branch() {
		int a = b;
		a = 5;
		if(a==5) {
			a = 6;
		}
		else {
			a = 7;
		}
		d = c;
		int c = 1;
		int d = e;
	}

	private void alias_loop() {
		int a = b;
		a = 5;
		if(a==5) {
			a = 6;
		}
		else {
			a = 7;
		}
		d = c;
		int c = 1;
		while(true) {
			int x =6;
			int y = 7;
		}
		System.out.println(a);
		int d = e;
	}

	public static double atan2(double y, double x) {
		final double epsilon = 1E-128;
		if (MathLib.abs(x) > epsilon) {
		    double temp = MathLib.atan(MathLib.abs(y) / MathLib.abs(x));
		    if (x < 0.0)
		        temp = PI - temp;
		    if (y < 0.0)
		        temp = TWO_PI - temp;
		    return temp;
		} else if (y > epsilon)
		    return HALF_PI;
		else if (y < -epsilon)
		    return 3 * HALF_PI;
		else
		    return 0.0;
	    }

	private void attachLog(@NonNull Intent intent) {
        Context context = getActivity();

        try {
            String log = Logcat.capture();
            if (log == null)
                throw new Exception("Failed to capture the logcat.");

            // Prepare cache directory.
            File cacheDir = context.getCacheDir();
            if (cacheDir == null)
                throw new Exception("Cache directory is inaccessible");
            File directory = new File(cacheDir, LogsProviderBase.DIRECTORY);
            FileUtils.deleteRecursive(directory); // Clean-up cache folder
            if (!directory.mkdirs())
                throw new Exception("Failed to create cache directory.");

            // Create log file.
            @SuppressLint("SimpleDateFormat")
            SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));
            String fileName = "AcDisplay_log_" + sdf.format(new Date()) + ".txt";
            File file = new File(directory, fileName);

            // Write to the file.
            if (!FileUtils.writeToFile(file, log))
                throw new Exception("Failed to write log to the file.");

            // Put extra stream to the intent.
            Uri uri = Uri.parse("content://" + LogAttachmentProvider.AUTHORITY + "/" + fileName);
            intent.putExtra(Intent.EXTRA_STREAM, uri);
        } catch (Exception e) {
            String message = ResUtils.getString(getResources(), R.string.feedback_error_accessing_log, e.getMessage());
            ToastUtils.showLong(context, message);
        }
    }

	private static String authenticateAdmobAccount(String currentAdmobAccount, Context context)
			throws AdmobRateLimitExceededException, AdmobInvalidTokenException,
			AdmobAccountRemovedException, AdmobAskForPasswordException,
			AdmobInvalidRequestException {

		String admobToken = AdmobAuthenticationUtilities.authenticateAccount(currentAdmobAccount,
				context);

		if (AdmobRequest.ERROR_RATE_LIMIT_EXCEEDED.equals(admobToken)) {
			throw new AdmobRateLimitExceededException(admobToken);
		} else if (AdmobRequest.ERROR_REQUEST_INVALID.equals(admobToken)) {
			throw new AdmobInvalidRequestException(admobToken);
		} else if (AdmobRequest.ERROR_ACCOUNT_REMOVED.equals(admobToken)) {
			throw new AdmobAccountRemovedException(admobToken, currentAdmobAccount);
		} else if (AdmobRequest.ERROR_ASK_USER_PASSWORD.equals(admobToken)) {
			throw new AdmobAskForPasswordException("Missing password");
		}

		return admobToken;
	}

	public void backToParent() {
        String[] arr = titlePath.split(getString(R.string.repo_path_root));
        titleName = arr[arr.length - 2];
        titlePath = arr[0];
        for (int i = 1; i < arr.length - 1; i++) {
            titlePath = titlePath + getString(R.string.repo_path_root) + arr[i];
        }
        titlePath = titlePath + getString(R.string.repo_path_root);

        actionBar.setTitle(titleName);
        actionBar.setSubtitle(titlePath);
        actionBar.setDisplayHomeAsUpEnabled(true);

        contentItemListBuffer.remove(contentItemListBuffer.size() - 1);
        List<ContentItem> contentItems = contentItemListBuffer.get(contentItemListBuffer.size() - 1);

        contentItemList.clear();
        for (ContentItem c: contentItems) {
            contentItemList.add(c);
        }
        contentItemAdapter.notifyDataSetChanged();
    }

	    public static Bitmap bitmap(@Nonnull final String content, final int size, int fgColor,
                                int bgColor, int margin) {
        try {
            final Hashtable<EncodeHintType, Object> hints = new Hashtable<EncodeHintType, Object>();
            hints.put(EncodeHintType.MARGIN, 0);
            hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            final BitMatrix result = QR_CODE_WRITER.encode(content, BarcodeFormat.QR_CODE, size,
                    size, hints);
            int[] drawBeginLocation = new int[]{0, 0};
            int dataWidth = result.getWidth();
            int dataHeight = result.getHeight();
            int outWidth = result.getWidth();
            int outHeight = result.getHeight();
            if (margin >= 0) {
                int[] drawRectangle = result.getEnclosingRectangle();
                int left = drawRectangle[0];
                int top = drawRectangle[1];
                int right = outWidth - drawRectangle[2] - left;
                int bottom = outHeight - drawRectangle[3] - top;
                int maxOriMargin = Math.max(Math.max(top, bottom), Math.max(left, right));
                if (margin > maxOriMargin) {
                    dataWidth = drawRectangle[2];
                    dataHeight = drawRectangle[3];
                    drawBeginLocation[0] = drawRectangle[0];
                    drawBeginLocation[1] = drawRectangle[1];
                    outWidth = dataWidth + margin * 2;
                    outHeight = dataHeight + margin * 2;
                }
            }
            final int[] pixels = new int[outWidth * outHeight];

            int startX = (outWidth - dataWidth) / 2;
            int startY = (outHeight - dataHeight) / 2;

            for (int y = 0;
                 y < outHeight;
                 y++) {
                final int offset = y * outWidth;
                for (int x = 0;
                     x < outWidth;
                     x++) {
                    if (x >= startX && x < dataWidth + startX && y >= startY && y < dataHeight +
                            startY) {
                        pixels[offset + x] = result.get(x - startX + drawBeginLocation[0],
                                y - startY + drawBeginLocation[1]) ? fgColor : bgColor;
                    } else {
                        pixels[offset + x] = bgColor;
                    }
                }
            }

            final Bitmap bitmap = Bitmap.createBitmap(outWidth, outHeight, Bitmap.Config.ARGB_8888);
            bitmap.setPixels(pixels, 0, outWidth, 0, 0, outWidth, outHeight);
            return bitmap;
        } catch (final WriterException x) {
            log.info("problem creating qr code", x);
            return null;
        }
    }

	  private void beautifyViews() {
    tvBody.setTextIsSelectable(true);
    tvBody.setMovementMethod(LinkMovementMethod.getInstance());

    tvZeroStateInfo.setTypeface(FontFactory.getRegular(this));
    tvRetry.setTypeface(FontFactory.getCondensedLight(this));
    tvTitle.setTypeface(FontFactory.getCondensedBold(this));
    tvBody.setTypeface(FontFactory.getRegular(this));
  }

	public int deadcode1() {
		int a = 5;
		int b = a + e;
		String str = "";
		int c = 0;
		while(b > 5) {
			c = c + 1;
		}
		String temp = str + "a";
		return -1;

	}

	public int initialised() {
		int a = 5;
		String str = new String();
		b = a + 6;
		int c;
		d = c + a;
		while(a > 5) {
			c = 5;
			int e = 3;
			f = c + 6;
			str = null;
		}		
		return 1;
	}
}
