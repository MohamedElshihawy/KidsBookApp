package com.example.book_v2.ui.activities // ktlint-disable package-name

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.book_v2.ui.fragments.ConstrainedMatchingFragment
import com.example.book_v2.ui.fragments.FreeMatchingFragment
import com.example.book_v2.ui.fragments.LetterFirstPreviewFragment
import com.example.book_v2.ui.fragments.LetterSecondPreviewFragment
import com.example.book.ui.fragments.SmallLetterFragment
import com.example.book_v2.ui.fragments.StoryTellingPageFragment
import com.example.book_v2.R
import com.example.book_v2.data.oop.Book
import com.example.book_v2.data.oop.ColoringPage
import com.example.book_v2.data.oop.ConstrainedMatchingPage
import com.example.book_v2.data.oop.FactoryBuilder
import com.example.book_v2.data.oop.FreeMatchingPage
import com.example.book_v2.data.oop.IntroductionPage
import com.example.book_v2.data.oop.LargeLetterPage
import com.example.book_v2.data.oop.LetterFirstPreviewPage
import com.example.book_v2.data.oop.LetterPredictionPage
import com.example.book_v2.data.oop.LetterPronunciationPage
import com.example.book_v2.data.oop.LetterSecondPreviewPage
import com.example.book_v2.data.oop.MediumLetterPage
import com.example.book_v2.data.oop.Page
import com.example.book_v2.data.oop.PageTypes
import com.example.book_v2.data.oop.SmallLetterPage
import com.example.book_v2.data.oop.StoryTellingPage
import com.example.book_v2.databinding.ActivityMainBinding
import com.example.book_v2.services.interfaces.PageNavListeners
import com.example.book_v2.ui.fragments.ColoringPageFragment
import com.example.book_v2.ui.fragments.IntroductionPageFragment
import com.example.book_v2.ui.fragments.LargeLetterFragment
import com.example.book_v2.ui.fragments.LetterPronunciationPageFragment
import com.example.book_v2.ui.fragments.MediumLetterFragment
import com.example.book_v2.ui.predictImage.PredictImageFragment

class MainActivity : AppCompatActivity(), PageNavListeners {
    private lateinit var binding: ActivityMainBinding
    private lateinit var bookPages: List<Page>
    private lateinit var book: Book
    private var currentPageNum = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(LayoutInflater.from(this))

        bookPages = FactoryBuilder.BuildBookPages(intent?.extras?.getStringArrayList("DATA"))

        bookPages = bookPages.sortedBy { it.pageNum }

        setContentView(binding.root)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
            )
        }

        for (i in bookPages) {
            Log.e("TAG", "onCreate: ${i.pageType}")
        }

//        val adapter = ViewPager2Adapter(supportFragmentManager, lifecycle)
//
//        for (i in bookPages.indices) {
//            adapter.addFragment(addFrags(i))
//        }
//
//
//        binding.viewPager.adapter = adapter
//
//        val bookFlipPageTransformer = BookFlipPageTransformer2()
//
//        bookFlipPageTransformer.isEnableScale = true
//
//        bookFlipPageTransformer.scaleAmountPercent = 10f
//
//        binding.viewPager.setPageTransformer(bookFlipPageTransformer)
//
//        binding.viewPager.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
//        adapter.stateRestorationPolicy = FragmentStateAdapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY
//        adapter.notifyDataSetChanged()
//        binding.viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
//            var lastPosition = 0
//            override fun onPageSelected(position: Int) {
//
//                super.onPageSelected(position)
//                if (position > lastPosition) {
//                    adapter.removeFragment(adapter.getFragment(0))
//                    Log.e("TAG", "onPageSelected: ${adapter.itemCount}")
//                }
//                lastPosition = position
//            }
//        })
//        adapter.stateRestorationPolicy = FragmentStateAdapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

        // binding.viewPager.isUserInputEnabled = false

        book = Book()
        book.bookPages = bookPages

        pagesNavigation(currentPageNum)
    }

    private fun pagesNavigation(pagePosition: Int) {
        val pageData = bookPages[pagePosition]
        val fragment: Fragment
        when (pageData.pageType) {
            PageTypes.INTRODUCTION_PAGE -> {
                fragment = IntroductionPageFragment(this, pageData as IntroductionPage)
                fragmentTransition(fragment)
            }

            PageTypes.LETTER_PAGE -> {
                fragment = LargeLetterFragment(this, pageData as LargeLetterPage)
                fragmentTransition(fragment)
            }

            PageTypes.MEDIUM_LETTER -> {
                fragment = MediumLetterFragment(this, pageData as MediumLetterPage)
                fragmentTransition(fragment)
            }

            PageTypes.SMALL_LETTER -> {
                fragment = SmallLetterFragment(this, pageData as SmallLetterPage)
                fragmentTransition(fragment)
            }

            PageTypes.FREE_MATCHING_PAGE -> {
                fragment = FreeMatchingFragment(this, pageData as FreeMatchingPage)
                fragmentTransition(fragment)
            }

            PageTypes.CONSTRAINED_MATCHING_PAGE -> {
                fragment = ConstrainedMatchingFragment(this, pageData as ConstrainedMatchingPage)
                fragmentTransition(fragment)
            }

            PageTypes.COLORING_PAGE -> {
                fragment = ColoringPageFragment(this, pageData as ColoringPage)

                fragmentTransition(fragment)
            }

            PageTypes.LETTER_FIRST_PREVIEW_PAGE -> {
                fragment = LetterFirstPreviewFragment(this, pageData as LetterFirstPreviewPage)
                fragmentTransition(fragment)
            }

            PageTypes.LETTER_SECOND_PREVIEW_PAGE -> {
                fragment = LetterSecondPreviewFragment(this, pageData as LetterSecondPreviewPage)
                fragmentTransition(fragment)
            }

            PageTypes.STORY_TELLING_PAGE -> {
                fragment = StoryTellingPageFragment(this, pageData as StoryTellingPage)
                fragmentTransition(fragment)
            }

            PageTypes.LETTER_PRONUNCIATION_PAGE -> {
                fragment =
                    LetterPronunciationPageFragment(this, pageData as LetterPronunciationPage)
                fragmentTransition(fragment)
            }

            PageTypes.LETTER_PREDICTION_PAGE -> {
                fragment =
                    PredictImageFragment(this, pageData as LetterPredictionPage)
                fragmentTransition(fragment)
            }

            else -> {
                throw Throwable("Error while transition to next page")
            }
        }
    }

    private fun fragmentTransition(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragments_container, fragment)
            .commit()
    }

    override fun nextPage() {
        if (currentPageNum < bookPages.size - 1) {
            currentPageNum++
            pagesNavigation(currentPageNum)
        }
    }

    override fun previousPage() {
        if (currentPageNum > 0) {
            currentPageNum--
            pagesNavigation(currentPageNum)
        } else {
            this.onBackPressed()
        }
    }
}
