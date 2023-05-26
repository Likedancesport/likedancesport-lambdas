package com.likedancesport.common.utils;

import com.likedancesport.common.aws.MediaConvertJobStateChangeEvent;
import com.likedancesport.common.aws.OutputDetail;
import com.likedancesport.common.aws.OutputGroupDetail;
import com.likedancesport.common.model.domain.HlsGroup;
import com.likedancesport.common.model.domain.S3Key;
import lombok.extern.slf4j.Slf4j;
import software.amazon.awssdk.services.mediaconvert.MediaConvertClient;
import software.amazon.awssdk.services.mediaconvert.model.AacAudioDescriptionBroadcasterMix;
import software.amazon.awssdk.services.mediaconvert.model.AacCodecProfile;
import software.amazon.awssdk.services.mediaconvert.model.AacCodingMode;
import software.amazon.awssdk.services.mediaconvert.model.AacRateControlMode;
import software.amazon.awssdk.services.mediaconvert.model.AacRawFormat;
import software.amazon.awssdk.services.mediaconvert.model.AacSettings;
import software.amazon.awssdk.services.mediaconvert.model.AacSpecification;
import software.amazon.awssdk.services.mediaconvert.model.AfdSignaling;
import software.amazon.awssdk.services.mediaconvert.model.AntiAlias;
import software.amazon.awssdk.services.mediaconvert.model.AudioCodec;
import software.amazon.awssdk.services.mediaconvert.model.AudioCodecSettings;
import software.amazon.awssdk.services.mediaconvert.model.AudioDescription;
import software.amazon.awssdk.services.mediaconvert.model.AudioLanguageCodeControl;
import software.amazon.awssdk.services.mediaconvert.model.AudioTypeControl;
import software.amazon.awssdk.services.mediaconvert.model.ColorMetadata;
import software.amazon.awssdk.services.mediaconvert.model.ContainerSettings;
import software.amazon.awssdk.services.mediaconvert.model.ContainerType;
import software.amazon.awssdk.services.mediaconvert.model.DescribeEndpointsRequest;
import software.amazon.awssdk.services.mediaconvert.model.DescribeEndpointsResponse;
import software.amazon.awssdk.services.mediaconvert.model.DropFrameTimecode;
import software.amazon.awssdk.services.mediaconvert.model.H264AdaptiveQuantization;
import software.amazon.awssdk.services.mediaconvert.model.H264CodecLevel;
import software.amazon.awssdk.services.mediaconvert.model.H264CodecProfile;
import software.amazon.awssdk.services.mediaconvert.model.H264DynamicSubGop;
import software.amazon.awssdk.services.mediaconvert.model.H264EntropyEncoding;
import software.amazon.awssdk.services.mediaconvert.model.H264FieldEncoding;
import software.amazon.awssdk.services.mediaconvert.model.H264FlickerAdaptiveQuantization;
import software.amazon.awssdk.services.mediaconvert.model.H264FramerateControl;
import software.amazon.awssdk.services.mediaconvert.model.H264FramerateConversionAlgorithm;
import software.amazon.awssdk.services.mediaconvert.model.H264GopBReference;
import software.amazon.awssdk.services.mediaconvert.model.H264GopSizeUnits;
import software.amazon.awssdk.services.mediaconvert.model.H264InterlaceMode;
import software.amazon.awssdk.services.mediaconvert.model.H264ParControl;
import software.amazon.awssdk.services.mediaconvert.model.H264QualityTuningLevel;
import software.amazon.awssdk.services.mediaconvert.model.H264QvbrSettings;
import software.amazon.awssdk.services.mediaconvert.model.H264RateControlMode;
import software.amazon.awssdk.services.mediaconvert.model.H264RepeatPps;
import software.amazon.awssdk.services.mediaconvert.model.H264SceneChangeDetect;
import software.amazon.awssdk.services.mediaconvert.model.H264Settings;
import software.amazon.awssdk.services.mediaconvert.model.H264SlowPal;
import software.amazon.awssdk.services.mediaconvert.model.H264SpatialAdaptiveQuantization;
import software.amazon.awssdk.services.mediaconvert.model.H264Syntax;
import software.amazon.awssdk.services.mediaconvert.model.H264Telecine;
import software.amazon.awssdk.services.mediaconvert.model.H264TemporalAdaptiveQuantization;
import software.amazon.awssdk.services.mediaconvert.model.H264UnregisteredSeiTimecode;
import software.amazon.awssdk.services.mediaconvert.model.HlsIFrameOnlyManifest;
import software.amazon.awssdk.services.mediaconvert.model.HlsSettings;
import software.amazon.awssdk.services.mediaconvert.model.M3u8NielsenId3;
import software.amazon.awssdk.services.mediaconvert.model.M3u8PcrControl;
import software.amazon.awssdk.services.mediaconvert.model.M3u8Scte35Source;
import software.amazon.awssdk.services.mediaconvert.model.M3u8Settings;
import software.amazon.awssdk.services.mediaconvert.model.MediaConvertException;
import software.amazon.awssdk.services.mediaconvert.model.Output;
import software.amazon.awssdk.services.mediaconvert.model.OutputSettings;
import software.amazon.awssdk.services.mediaconvert.model.RespondToAfd;
import software.amazon.awssdk.services.mediaconvert.model.ScalingBehavior;
import software.amazon.awssdk.services.mediaconvert.model.TimedMetadata;
import software.amazon.awssdk.services.mediaconvert.model.VideoCodec;
import software.amazon.awssdk.services.mediaconvert.model.VideoCodecSettings;
import software.amazon.awssdk.services.mediaconvert.model.VideoDescription;
import software.amazon.awssdk.services.mediaconvert.model.VideoTimecodeInsertion;

import java.net.URI;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.OptionalLong;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

@Slf4j
public class MediaConvertUtils {
    private static final Pattern NAME_MODIFIER_PATTERN = Pattern.compile("_(\\d+p).");

    public static void withMediaConvertClient(Consumer<MediaConvertClient> operation) {
        try (MediaConvertClient mc = MediaConvertClient.create()) {
            log.info("---- Building MediaConvertClient");
            DescribeEndpointsResponse res = mc
                    .describeEndpoints(DescribeEndpointsRequest.builder().maxResults(20).build());
            String endpointURL = res.endpoints().get(0).url();

            MediaConvertClient mediaConvertClient = MediaConvertClient.builder()
                    .region(mc.serviceClientConfiguration().region())
                    .endpointOverride(URI.create(endpointURL))
                    .build();


            operation.accept(mediaConvertClient);
        }
    }

    public static HlsGroup getHlsGroup(MediaConvertJobStateChangeEvent mediaConvertJobStateChangeEvent) {
        List<S3Key> s3Keys = mediaConvertJobStateChangeEvent.getDetail().getOutputGroupDetails().stream()
                .map(OutputGroupDetail::getOutputDetails)
                .flatMap(List::stream)
                .map(OutputDetail::getOutputFilePaths)
                .flatMap(List::stream)
                .map(S3Key::ofUri)
                .collect(Collectors.toList());

        String initialFolder = S3Utils.extractFolderFromKey(s3Keys.get(0));

        if (!s3Keys.stream().allMatch(s3Key -> S3Utils.extractFolderFromKey(s3Key).equals(initialFolder))) {
            throw new RuntimeException("Transcoding Job Invalid");
        }

        Map<String, S3Key> s3KeyMap = s3Keys.stream()
                .collect(Collectors.toMap(MediaConvertUtils::getNameModifier, Function.identity()));

        return HlsGroup.builder()
                .folderKey(S3Key.of(s3Keys.get(0).getBucketName(), initialFolder))
                .hlsVideos(s3KeyMap)
                .build();
    }

    public static Duration getDuration(MediaConvertJobStateChangeEvent event) {
        Long durationInMillis = event.getDetail().getOutputGroupDetails().stream()
                .map(OutputGroupDetail::getOutputDetails)
                .map(outputDetails -> outputDetails.stream().mapToLong(OutputDetail::getDurationInMs))
                .map(LongStream::max)
                .map(OptionalLong::orElseThrow)
                .max(Long::compareTo)
                .orElseThrow();
        return Duration.ofMillis(durationInMillis);
    }

    public static String getNameModifier(S3Key s3Key) {
        return NAME_MODIFIER_PATTERN.matcher(s3Key.getKey()).group(1);
    }

    public static Output createOutput(String nameModifier,
                                      String segmentModifier,
                                      int qvbrMaxBitrate,
                                      int qvbrQualityLevel,
                                      int targetWidth,
                                      int targetHeight) {
        log.info("Creating {} output", nameModifier);
        Output output = null;
        try {
            output = Output.builder().nameModifier(nameModifier).outputSettings(OutputSettings.builder()
                            .hlsSettings(HlsSettings.builder().segmentModifier(segmentModifier).audioGroupId("program_audio")
                                    .iFrameOnlyManifest(HlsIFrameOnlyManifest.EXCLUDE).build())
                            .build())
                    .containerSettings(getContainerSettings())
                    .videoDescription(getVideoDescription(qvbrMaxBitrate, qvbrQualityLevel, targetWidth, targetHeight))
                    .audioDescriptions(getAudioDescription())
                    .build();
        } catch (MediaConvertException e) {
            e.printStackTrace();
            System.exit(0);
        }
        return output;
    }

    private static ContainerSettings getContainerSettings() {
        return ContainerSettings.builder().container(ContainerType.M3_U8)
                .m3u8Settings(M3u8Settings.builder().audioFramesPerPes(4)
                        .pcrControl(M3u8PcrControl.PCR_EVERY_PES_PACKET).pmtPid(480).privateMetadataPid(503)
                        .programNumber(1).patInterval(0).pmtInterval(0).scte35Source(M3u8Scte35Source.NONE)
                        .scte35Pid(500).nielsenId3(M3u8NielsenId3.NONE).timedMetadata(TimedMetadata.NONE)
                        .timedMetadataPid(502).videoPid(481)
                        .audioPids(482, 483, 484, 485, 486, 487, 488, 489, 490, 491, 492).build())
                .build();
    }

    private static AudioDescription getAudioDescription() {
        return AudioDescription.builder().audioTypeControl(AudioTypeControl.FOLLOW_INPUT)
                .languageCodeControl(AudioLanguageCodeControl.FOLLOW_INPUT)
                .codecSettings(AudioCodecSettings.builder().codec(AudioCodec.AAC).aacSettings(AacSettings
                                .builder().codecProfile(AacCodecProfile.LC).rateControlMode(AacRateControlMode.CBR)
                                .codingMode(AacCodingMode.CODING_MODE_2_0).sampleRate(44100).bitrate(96000)
                                .rawFormat(AacRawFormat.NONE).specification(AacSpecification.MPEG4)
                                .audioDescriptionBroadcasterMix(AacAudioDescriptionBroadcasterMix.NORMAL).build())
                        .build())
                .build();
    }

    private static VideoDescription getVideoDescription(int qvbrMaxBitrate, int qvbrQualityLevel, int targetWidth, int targetHeight) {
        return VideoDescription.builder().width(targetWidth).height(targetHeight)
                .scalingBehavior(ScalingBehavior.DEFAULT).sharpness(50).antiAlias(AntiAlias.ENABLED)
                .timecodeInsertion(VideoTimecodeInsertion.DISABLED)
                .colorMetadata(ColorMetadata.INSERT).respondToAfd(RespondToAfd.NONE)
                .afdSignaling(AfdSignaling.NONE).dropFrameTimecode(DropFrameTimecode.ENABLED)
                .codecSettings(VideoCodecSettings.builder().codec(VideoCodec.H_264)
                        .h264Settings(getH264Settings(qvbrMaxBitrate, qvbrQualityLevel, targetWidth, targetHeight))
                        .build())
                .build();
    }

    private static H264Settings getH264Settings(int qvbrMaxBitrate, int qvbrQualityLevel, int targetWidth, int targetHeight) {
        return H264Settings.builder()
                .rateControlMode(H264RateControlMode.QVBR)
                .parControl(H264ParControl.INITIALIZE_FROM_SOURCE)
                .qualityTuningLevel(H264QualityTuningLevel.SINGLE_PASS)
                .qvbrSettings(H264QvbrSettings.builder()
                        .qvbrQualityLevel(qvbrQualityLevel).build())
                .codecLevel(H264CodecLevel.AUTO)
                .codecProfile((targetHeight > 720 && targetWidth > 1280)
                        ? H264CodecProfile.HIGH
                        : H264CodecProfile.MAIN)
                .maxBitrate(qvbrMaxBitrate)
                .framerateControl(H264FramerateControl.INITIALIZE_FROM_SOURCE)
                .gopSize(2.0).gopSizeUnits(H264GopSizeUnits.SECONDS)
                .numberBFramesBetweenReferenceFrames(2).gopClosedCadence(1)
                .gopBReference(H264GopBReference.DISABLED)
                .slowPal(H264SlowPal.DISABLED).syntax(H264Syntax.DEFAULT)
                .numberReferenceFrames(3).dynamicSubGop(H264DynamicSubGop.STATIC)
                .fieldEncoding(H264FieldEncoding.PAFF)
                .sceneChangeDetect(H264SceneChangeDetect.ENABLED).minIInterval(0)
                .telecine(H264Telecine.NONE)
                .framerateConversionAlgorithm(H264FramerateConversionAlgorithm.DUPLICATE_DROP)
                .entropyEncoding(H264EntropyEncoding.CABAC).slices(1)
                .unregisteredSeiTimecode(H264UnregisteredSeiTimecode.DISABLED)
                .repeatPps(H264RepeatPps.DISABLED)
                .adaptiveQuantization(H264AdaptiveQuantization.HIGH)
                .spatialAdaptiveQuantization(H264SpatialAdaptiveQuantization.ENABLED)
                .temporalAdaptiveQuantization(H264TemporalAdaptiveQuantization.ENABLED)
                .flickerAdaptiveQuantization(H264FlickerAdaptiveQuantization.DISABLED)
                .softness(0)
                .interlaceMode(H264InterlaceMode.PROGRESSIVE)
                .build();
    }
}